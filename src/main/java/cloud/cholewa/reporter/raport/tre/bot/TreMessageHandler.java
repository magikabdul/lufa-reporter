package cloud.cholewa.reporter.raport.tre.bot;

import cloud.cholewa.reporter.raport.tre.model.TreRaportContext;
import cloud.cholewa.reporter.raport.tre.service.TreService;
import cloud.cholewa.reporter.raport.tre.status.TreStatus;
import io.github.natanimn.telebof.BotContext;
import io.github.natanimn.telebof.annotations.MessageHandler;
import io.github.natanimn.telebof.types.updates.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static io.github.natanimn.telebof.enums.ParseMode.MARKDOWN;

@Slf4j
@Service
@RequiredArgsConstructor
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

    private TreRaportContext raportContext;

    @MessageHandler(commands = "help")
    void handleHelp(final BotContext ctx, Message message) {
        log.info("Help command received");
        ctx.sendMessage(
            message.chat.id,
            """
                `/start` - rozpoczyna wprowadzanie raportu
                `/cancel` - anuluje wprowadzanie raportu
                """
        ).exec();
    }

    @MessageHandler(commands = "start")
    void handleStart(final BotContext ctx, final Message message) {
        log.info("Start command received: {}", message.text);

        if (raportContext != null) {
            log.info("Raport preparation started");
            treStatus.setInProgress(true);

            ctx.sendMessage(
                message.chat.id,
                """
                    Dotychczasowe wprowadzanie raportu nie zostało zakończone.
                    Jeżeli potrzebujesz uzyskać informacje o bieżącym stanie wprowadzanego raportu, wpisz */status*.
                    """
            ).parseMode(MARKDOWN).exec();
        } else {
            raportContext = new TreRaportContext(LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
            log.info("Raport preparation started at: {}", raportContext.getCreatedDate());
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
        raportContext.setCustomer(message.text);
    }

    @MessageHandler(state = DESCRIPTION)
    void handleDescription(final BotContext ctx, final Message message) {
        log.info("Description received: {}", message.text);
        ctx.sendMessage(message.chat.id, "Podaj ilość spędzonych godzin").exec();
        ctx.setState(message.chat.id, HOURS);
        ctx.getStateData(message.chat.id).put(DESCRIPTION, message.text);
        raportContext.setDescription(message.text);
    }

    @MessageHandler(state = HOURS)
    void handleHours(final BotContext ctx, final Message message) {
        log.info("Spent hours received: {}", message.text);
        ctx.sendMessage(message.chat.id, "Podaj imię handlowca").exec();
        ctx.setState(message.chat.id, FIRSTNAME);
        ctx.getStateData(message.chat.id).put(HOURS, message.text);
        raportContext.setHours(Integer.parseInt(message.text));
    }

    @MessageHandler(state = FIRSTNAME)
    void handleFirstname(final BotContext ctx, final Message message) {
        log.info("Salesman's firstname received: {}", message.text);
        ctx.sendMessage(message.chat.id, "Podaj nazwisko handlowca").exec();
        ctx.setState(message.chat.id, LASTNAME);
        ctx.getStateData(message.chat.id).put(FIRSTNAME, message.text);
        raportContext.setSalesmanFirstName(message.text);
    }

    @MessageHandler(state = LASTNAME)
    void handleLastname(final BotContext ctx, final Message message) {
        log.info("Salesman's lastname received: {}", message.text);
        ctx.getStateData(message.chat.id).put(LASTNAME, message.text);
        raportContext.setSalesmanLastName(message.text);
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
            raportContext.setNotes(message.text);
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

            treService.saveReport(raportContext)
                .doOnSuccess(unused -> {
                    ctx.sendMessage(message.chat.id, "Raport zapisany").exec();
                    ctx.clearState(message.chat.id);
                    raportContext = null;
                    treStatus.setInProgress(false);
                    treStatus.setStatusReported(true);
                })
                .subscribe();
        } else {
            log.info("Provided report discarded");
            ctx.sendMessage(message.chat.id, "Raport odrzucony").exec();
            ctx.clearState(message.chat.id);
            raportContext = null;
            treStatus.setInProgress(false);
        }
    }

    @MessageHandler(commands = "end")
    void handleEnd(final BotContext ctx, Message message) {
        log.info("Finishing of the report for Tre");

        if (raportContext == null) {
            ctx.sendMessage(
                message.chat.id,
                """
                    Nie zostały podane wszystkie informacje potrzebne do wprowadzania raportu.
                    Jeżeli chcesz przerwać wprowadzanie raportu, wpisz `/cancel`.
                    """
            ).parseMode(MARKDOWN).exec();
        }
    }
}
