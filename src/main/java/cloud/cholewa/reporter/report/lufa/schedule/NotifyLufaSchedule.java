package cloud.cholewa.reporter.report.lufa.schedule;

import cloud.cholewa.reporter.report.lufa.service.LufaReportStatus;
import cloud.cholewa.reporter.telegram.client.TelegramClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static cloud.cholewa.reporter.telegram.model.StatusType.NOT_REPORTED;
import static cloud.cholewa.reporter.telegram.model.StatusType.REPORTED;
import static cloud.cholewa.reporter.telegram.model.StatusType.SKIPPED;
import static cloud.cholewa.reporter.telegram.model.VendorName.LUFA;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyLufaSchedule {

    private final TelegramClient telegramClient;
    private final LufaReportStatus status;

    @Scheduled(cron = "0 0 21 * * *", zone = "Europe/Warsaw")
    void sendNotification() {
        log.info("Sending notification to LufaBot about today tasks");
        status.setStatus(NOT_REPORTED);

        telegramClient
            .sendMessage(
                LUFA, """
                    Podaj informację nad jakimi zadaniami dzisiaj pracowałeś?
                    Rozpocznij wprowadzanie raportu podając polecenie `/start`.
                    Jeżeli chcesz pominąć ten krok, wpisz `/skip`.
                    """
            )
            .subscribe();
    }

    @Scheduled(cron = "0 0/30 22-23 * * *", zone = "Europe/Warsaw")
    void resendNotification() {

        if (status.getStatus() == null) {
            return;
        }

        if (status.getStatus() != REPORTED && status.getStatus() != SKIPPED) {
            log.info("Resending notification to LufaBot about today tasks while it was not reported today");

            telegramClient
                .sendMessage(
                    LUFA,
                    """
                        Dzisiaj jeszcze nie zaraportowałeś wykonanych prac.
                        Rozpocznij wprowadzanie raportu podając polecenie `/start`.
                        Jeżeli chcesz pominąć ten krok, wpisz `/skip`.
                        """
                )
                .subscribe();
        }
    }
}
