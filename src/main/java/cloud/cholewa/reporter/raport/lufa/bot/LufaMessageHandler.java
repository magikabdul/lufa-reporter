package cloud.cholewa.reporter.raport.lufa.bot;

import cloud.cholewa.reporter.raport.lufa.model.LufaReportContext;
import cloud.cholewa.reporter.raport.lufa.service.CategorizeService;
import cloud.cholewa.reporter.raport.lufa.service.LufaReportStatus;
import cloud.cholewa.reporter.raport.lufa.service.LufaService;
import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.types.updates.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static cloud.cholewa.reporter.telegram.model.StatusType.IN_PROGRESS;
import static cloud.cholewa.reporter.telegram.model.StatusType.NOT_REPORTED;
import static cloud.cholewa.reporter.telegram.model.StatusType.REPORTED;
import static cloud.cholewa.reporter.telegram.model.StatusType.SKIPPED;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class LufaMessageHandler {

    private final LufaReportStatus status;
    private final CategorizeService categorizeService;
    private final LufaService lufaService;

    private LufaReportContext raport;

    @MessageHandler(commands = "help")
    void handleHelp(final BotContext ctx, final Message message) {
        log.info("Help command received");
        ctx.sendMessage(
            message.chat.id,
            """
                `/start` - rozpoczyna wprowadzanie raportu
                `/cancel` - anuluje wprowadzanie raportu
                `/skip` - pomija krok wprowadzania raportu dla danego dnia
                """
        ).exec();
    }

    @MessageHandler(commands = "skip")
    void handleSkip(final BotContext ctx, final Message message) {
        log.info("Skip command received: {}", message.text);
        status.setStatus(SKIPPED);
    }

    @MessageHandler(commands = "cancel")
    void handleCancel(final BotContext ctx, final Message message) {
        log.info("Cancel command received: {}", message.text);
        status.setStatus(NOT_REPORTED);
    }

    @MessageHandler(commands = "start")
    void handleStart(final BotContext ctx, final Message message) {
        log.info("Start command received: {}", message.text);

        if (raport != null) {
            log.info("Report for Lufthansa preparation in progress");
            status.setStatus(IN_PROGRESS);
            ctx.sendMessage(
                message.chat.id,
                """
                    Dotychczasowe wprowadzanie raportu nie zostało zakończone.
                    Jeżeli chcesz go zakończyć wpisz `/cancel`.
                    """
            ).exec();
        } else {
            raport = new LufaReportContext(LocalDate.now());
            log.info("Report for Lufthansa preparation started at: {}", raport.getCreatedDate());
            ctx.sendMessage(message.chat.id, "Podaj opis zadania, nad którym pracowałeś").exec();
            ctx.setState(message.chat.id, "DESCRIPTION");
        }
    }

    @MessageHandler(state = "DESCRIPTION")
    void handleDescription(final BotContext ctx, final Message message) {
        log.info("Task description received: {}", message.text);

        ctx.sendMessage(message.chat.id, "Typ zadania wysłany do AI, trwa określanie typu zadania ...").exec();

        categorizeService.categorize(message.text)
            .map(taskChatResponse -> {
                raport.setCategory(taskChatResponse.getCategory());
                raport.setDescription(taskChatResponse.getDescription());
                return raport;
            })
            .doOnNext(reportContext -> processConfirmationState(ctx, message))
            .subscribe();

    }

    private void processConfirmationState(final BotContext ctx, final Message message) {
        ctx.sendMessage(
            message.chat.id,
            String.format(
                """
                    #Status raportu#
                    
                    *Typ zadania*: %s
                    *Opis*: %s
                    
                    **Potwierdzenie zapisu (y/n)**
                    """,
                raport.getCategory(),
                raport.getDescription()
            )
        ).exec();
        ctx.setState(message.chat.id, "CONFIRM");
    }

    @MessageHandler(state = "CONFIRM")
    void handleConfirm(final BotContext ctx, final Message message) {
        if (message.text.equalsIgnoreCase("y")) {
            log.info("Confirmed report");

            lufaService.saveReport(raport).subscribe();

            ctx.sendMessage(message.chat.id, "Raport zapisany").exec();
            ctx.clearState(message.chat.id);
            raport = null;
            status.setStatus(REPORTED);
        } else {
            log.info("Rejected report");
            ctx.sendMessage(message.chat.id, "Raport odrzucony").exec();
            ctx.clearState(message.chat.id);
            raport = null;
            status.setStatus(NOT_REPORTED);
        }
    }
}
