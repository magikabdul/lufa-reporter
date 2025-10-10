package cloud.cholewa.reporter.raport.lufa.schedule;

import cloud.cholewa.reporter.telegram.client.TelegramClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static cloud.cholewa.reporter.telegram.model.VendorName.LUFA;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyLufaSchedule {

    private final TelegramClient telegramClient;

    @Scheduled(cron = "0 0 21 * * *", zone = "Europe/Warsaw")
    void sendNotification() {
        log.info("Sending notification to LufaBot about today tasks");
        telegramClient.sendMessage(
            LUFA, """
                Podaj informację nad jakimi taskami pracowałeś dzisiaj
                Rozpocznij wprowadzanie raportu podając polecenie `/start`.
                Jeżeli chcesz pominąć ten krok, wpisz `/skip`.
                """
        ).subscribe();
    }
}
