package cloud.cholewa.reporter.raport.tre.bot;

import cloud.cholewa.reporter.raport.tre.model.TreReportContext;
import cloud.cholewa.reporter.raport.tre.service.TreService;
import cloud.cholewa.reporter.raport.tre.status.TreStatus;
import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.types.updates.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static cloud.cholewa.reporter.raport.tre.status.TreStatus.TreStatusType.IN_PROGRESS;
import static cloud.cholewa.reporter.raport.tre.status.TreStatus.TreStatusType.NOT_REPORTED;
import static cloud.cholewa.reporter.raport.tre.status.TreStatus.TreStatusType.REPORTED;
import static cloud.cholewa.reporter.raport.tre.status.TreStatus.TreStatusType.SKIPPED;
import static io.github.natanimn.telebof.enums.ParseMode.MARKDOWN;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class TreMessageHandler {

    private static final String COMPANY = "company";
    private static final String DESCRIPTION = "description";
    private static final String HOURS = "hours";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String NOTES = "notes";
    private static final String CONFIRM = "confirm";

    private final TreStatus treStatus;
    private final TreService treService;

    private TreReportContext reportContext;

    @MessageHandler(commands = "help")
    void handleHelp(final BotContext ctx, Message message) {
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
        treStatus.setStatus(SKIPPED);
    }

    @MessageHandler(commands = "cancel")
    void handleCancel(final BotContext ctx, final Message message) {
        log.info("Chancel command received: {}", message.text);
        treStatus.setStatus(NOT_REPORTED);
    }

    @MessageHandler(commands = "start")
    void handleStart(final BotContext ctx, final Message message) {
        log.info("Start command received: {}", message.text);

        if (reportContext != null) {
            log.info("Raport preparation started");
            treStatus.setStatus(IN_PROGRESS);

            ctx.sendMessage(
                message.chat.id,
                """
                    Dotychczasowe wprowadzanie raportu nie zostało zakończone.
                    Jeżeli potrzebujesz uzyskać informacje o bieżącym stanie wprowadzanego raportu, wpisz */status*.
                    """
            ).parseMode(MARKDOWN).exec();
        } else {
            reportContext = new TreReportContext(LocalDate.now());
            log.info("Raport preparation started at: {}", reportContext.getCreatedDate());
            ctx.sendMessage(message.chat.id, "Podaj nazwę klienta, dla którego wprowadzasz raport").exec();
            ctx.setState(message.chat.id, COMPANY);
        }
    }

    @MessageHandler(state = COMPANY)
    void handleCompany(final BotContext ctx, final Message message) {
        log.info("Company name received: {}", message.text);
        ctx.sendMessage(message.chat.id, "Podaj opis wykonanych czynności").exec();
        ctx.setState(message.chat.id, DESCRIPTION);
        ctx.getStateData(message.chat.id).put(COMPANY, message.text);
        reportContext.setCustomer(message.text);
    }

    @MessageHandler(state = DESCRIPTION)
    void handleDescription(final BotContext ctx, final Message message) {
        log.info("Description received: {}", message.text);
        ctx.sendMessage(message.chat.id, "Podaj ilość spędzonych godzin").exec();
        ctx.setState(message.chat.id, HOURS);
        ctx.getStateData(message.chat.id).put(DESCRIPTION, message.text);
        reportContext.setDescription(message.text);
    }

    @MessageHandler(state = HOURS)
    void handleHours(final BotContext ctx, final Message message) {
        log.info("Spent hours received: {}", message.text);
        ctx.sendMessage(message.chat.id, "Podaj imię handlowca").exec();
        ctx.setState(message.chat.id, FIRSTNAME);
        ctx.getStateData(message.chat.id).put(HOURS, message.text);
        reportContext.setHours(Integer.parseInt(message.text));
    }

    @MessageHandler(state = FIRSTNAME)
    void handleFirstname(final BotContext ctx, final Message message) {
        log.info("Salesman's firstname received: {}", message.text);
        ctx.sendMessage(message.chat.id, "Podaj nazwisko handlowca").exec();
        ctx.setState(message.chat.id, LASTNAME);
        ctx.getStateData(message.chat.id).put(FIRSTNAME, message.text);
        reportContext.setSalesmanFirstName(message.text);
    }

    @MessageHandler(state = LASTNAME)
    void handleLastname(final BotContext ctx, final Message message) {
        log.info("Salesman's lastname received: {}", message.text);
        ctx.getStateData(message.chat.id).put(LASTNAME, message.text);
        reportContext.setSalesmanLastName(message.text);
        ctx.sendMessage(
                message.chat.id, "Opcjonalnie podaj dodatkowe informacje.\nWpisz `n` jeżeli pomijasz ten krok.")
            .parseMode(MARKDOWN).exec();
        ctx.setState(message.chat.id, NOTES);
    }

    @MessageHandler(state = NOTES)
    void handleNotes(final BotContext ctx, final Message message) {
        log.info("Notes received: {}", message.text);
        if (!message.text.equalsIgnoreCase("n")) {
            ctx.getStateData(message.chat.id).put(NOTES, message.text);
            reportContext.setNotes(message.text);
        }

        ctx.sendMessage(
                message.chat.id,
                String.format(
                    """
                        #Wprowadzono następujące informacje
                        *Nazwa klienta*: %s
                        *Opis*: %s
                        *Ilość godzin*: %s
                        *Imię handlowca*: %s
                        *Nazwisko handlowca*: %s
                        *Dodatkowe informacje*: %s
                        
                        **Potwierdzenie zapisu (y/n)**
                        """,
                    ctx.getStateData(message.chat.id).get(COMPANY),
                    ctx.getStateData(message.chat.id).get(DESCRIPTION),
                    ctx.getStateData(message.chat.id).get(HOURS),
                    ctx.getStateData(message.chat.id).get(FIRSTNAME),
                    ctx.getStateData(message.chat.id).get(LASTNAME),
                    ctx.getStateData(message.chat.id).getOrDefault(NOTES, "Brak")
                )
            )
            .parseMode(MARKDOWN).exec();
        ctx.setState(message.chat.id, CONFIRM);
    }

    @MessageHandler(state = CONFIRM)
    void handleConfirm(final BotContext ctx, final Message message) {
        if (message.text.equalsIgnoreCase("y")) {

            treService.saveReport(reportContext)
                .doOnSuccess(unused -> {
                    ctx.sendMessage(message.chat.id, "Raport zapisany").exec();
                    ctx.clearState(message.chat.id);
                    reportContext = null;
                    treStatus.setStatus(REPORTED);
                })
                .subscribe();
        } else {
            log.info("Provided report discarded");
            ctx.sendMessage(message.chat.id, "Raport odrzucony").exec();
            ctx.clearState(message.chat.id);
            reportContext = null;
            treStatus.setStatus(NOT_REPORTED);
        }
    }
}
