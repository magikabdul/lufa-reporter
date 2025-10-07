package cloud.cholewa.reporter.raport.tre.schedule;

import cloud.cholewa.reporter.raport.tre.status.TreStatus;
import cloud.cholewa.reporter.telegram.client.TelegramClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static cloud.cholewa.reporter.telegram.model.VendorName.TRE;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyTreSchedule {

    private final TelegramClient telegramClient;
    private final TreStatus treStatus;

    @Scheduled(cron = "0 0 20 * * *", zone = "Europe/Warsaw")
    void sendNotification() {
        log.info("Send notification to Tre with report request");
        treStatus.setStatusReported(false);

        telegramClient
            .sendMessage(
                TRE, """
                    Podaj informację o rzeczach które dzisiaj wykonałeś.
                    Rozpocznij wprowadzanie raportu podając polecenie `/start`.
                    """
            )
            .subscribe();
    }

    @Scheduled(cron = "0 0/30 21-23 * * *", zone = "Europe/Warsaw")
    void resendNotification() {
        if (!treStatus.isStatusReported() && !treStatus.isInProgress()) {
            log.info("Resend notification to Tre with report request while is was not reported today");

            telegramClient
                .sendMessage(
                    TRE, """
                        Dzisiaj jeszcze nie zaraportowałeś wykonanych rzeczy.
                        Rozpocznij wprowadzanie raportu podając polecenie `/start`.
                        """
                )
                .subscribe();
        } else if (treStatus.isInProgress()) {
            log.info("Remainder notification to Tre the report is in progress");

            telegramClient
                .sendMessage(
                    TRE, """
                        Nie zakończyłeś wprowadzania raportu.
                        Jeżeli chcesz przerwać wprowadzanie raportu, wpisz `/cancel`.
                        """
                )
                .subscribe();
        }
    }
}
