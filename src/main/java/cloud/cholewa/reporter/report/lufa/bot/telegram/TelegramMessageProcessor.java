package cloud.cholewa.reporter.report.lufa.bot.telegram;

import cloud.cholewa.reporter.report.lufa.service.CategorizeService;
import cloud.cholewa.reporter.report.lufa.service.ReportCreatorService;
import cloud.cholewa.reporter.report.lufa.service.ReportSubmittingService;
import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.types.updates.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class TelegramMessageProcessor {

    private static final String COMMAND_START = "start";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_SKIP = "skip";
    private static final String COMMAND_CANCEL = "cancel";
    private static final String STATE_DESCRIPTION = "description";
    private static final String STATE_CONFIRM = "confirm";

    private final ReportCreatorService reportCreatorService;

    //    private final LufaReportStatus status;
    private final CategorizeService categorizeService;
    private final ReportSubmittingService reportSubmittingService;

//    private DailyReportContext raport;

    @MessageHandler(commands = COMMAND_START)
    void handleHelp(final BotContext ctx, final Message message) {
        log.info("Lufa Telegram bot - help command received");
        ctx.sendMessage(
            message.chat.id,
            """
                `/start` - rozpoczyna wprowadzanie raportu
                `/cancel` - anuluje wprowadzanie raportu
                `/skip` - pomija krok wprowadzania raportu dla danego dnia
                """
        ).exec();
    }

//    @MessageHandler(commands = COMMAND_SKIP)
//    void handleSkip(final BotContext ctx, final Message message) {
//        reportCreatorService.skipReport()
//            .then(Mono.just(ctx.sendMessage(message.chat.id, "Pomijano krok wprowadzania raportu").exec()))
//            .doOnSubscribe(subscription -> log.info("Lufa Telegram bot - skip command received"))
//            .subscribe();
//    }

//    @MessageHandler(commands = COMMAND_CANCEL)
//    void handleCancel(final BotContext ctx, final Message message) {
//        reportCreatorService.initReport()
//            .doOnSubscribe(subscription -> log.info("Lufa Telegram bot - cancel command received"))
//            .subscribe();
//    }
//
//    @MessageHandler(commands = COMMAND_START)
//    void handleStart(final BotContext ctx, final Message message) {
//        reportCreatorService.startReporting()
//            .doOnSubscribe(subscription -> log.info("Lufa Telegram bot - start command received"));
//
//
//        log.info("Start command received: {}", message.text);

//        if (raport != null) {
//            log.info("Report for Lufthansa preparation in progress");
//            status.setStatus(IN_PROGRESS);
//            ctx.sendMessage(
//                message.chat.id,
//                """
//                    Dotychczasowe wprowadzanie raportu nie zostało zakończone.
//                    Jeżeli chcesz go zakończyć wpisz `/cancel`.
//                    """
//            ).exec();
//        } else {
//            raport = new DailyReportContext(LocalDate.now());
//            log.info("Report for Lufthansa preparation started at: {}", raport.getCreatedDate());
//            ctx.sendMessage(message.chat.id, "Podaj opis zadania, nad którym pracowałeś").exec();
//            ctx.setState(message.chat.id, STATE_DESCRIPTION);
//        }
//    }

//    @MessageHandler(state = STATE_DESCRIPTION)
//    void handleDescription(final BotContext ctx, final Message message) {
//        log.info("Task description received: {}", message.text);
//
//        ctx.sendMessage(message.chat.id, "Typ zadania wysłany do AI, trwa określanie typu zadania ...").exec();

//        categorizeService.categorize(message.text)
//            .map(taskChatResponse -> {
//                raport.setCategory(taskChatResponse.getCategory());
//                raport.setDescription(taskChatResponse.getDescription());
//                return raport;
//            })
//            .doOnNext(reportContext -> processConfirmationState(ctx, message))
//            .subscribe();

//    }

    private void processConfirmationState(final BotContext ctx, final Message message) {
//        ctx.sendMessage(
//            message.chat.id,
//            String.format(
//                """
//                    #Status raportu#
//
//                    *Typ zadania*: %s
//                    *Opis*: %s
//
//                    **Potwierdzenie zapisu (y/n)**
//                    """,
//                raport.getCategory(),
//                raport.getDescription()
//            )
//        ).exec();
//        ctx.setState(message.chat.id, STATE_CONFIRM);
    }

    @MessageHandler(state = STATE_CONFIRM)
    void handleConfirm(final BotContext ctx, final Message message) {
//        if (message.text.equalsIgnoreCase("y")) {
//            log.info("Confirmed report");
//
//            reportService.saveReport(raport).subscribe();
//
//            ctx.sendMessage(message.chat.id, "Raport zapisany").exec();
//            ctx.clearState(message.chat.id);
//            raport = null;
//            status.setStatus(REPORTED);
//        } else {
//            log.info("Rejected report");
//            ctx.sendMessage(message.chat.id, "Raport odrzucony").exec();
//            ctx.clearState(message.chat.id);
//            raport = null;
//            status.setStatus(NOT_REPORTED);
//        }
    }
}
