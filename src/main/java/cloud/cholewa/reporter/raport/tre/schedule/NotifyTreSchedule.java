package cloud.cholewa.reporter.raport.tre.schedule;

import cloud.cholewa.reporter.telegram.client.TelegramClient;
import cloud.cholewa.reporter.telegram.model.VendorName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyTreSchedule {

    private final TelegramClient telegramClient;

    @Scheduled(cron = "0 0 20 * * *", zone = "Europe/Warsaw")
    void sendNotification() {
        log.info("Podaj informację o rzeczach które dzisiaj wykonałeś");
        telegramClient.sendMessage(VendorName.TRE, "Podaj informację o rzeczach które dzisiaj wykonałeś").subscribe();
    }
}
