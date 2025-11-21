package cloud.cholewa.reporter.report.lufa.schedule;

import cloud.cholewa.reporter.report.lufa.bot.discord.DiscordService;
import cloud.cholewa.reporter.report.lufa.service.ReportCreatorService;
import cloud.cholewa.reporter.report.lufa.service.ReportSubmittingService;
import cloud.cholewa.reporter.telegram.client.TelegramClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static cloud.cholewa.reporter.model.VendorName.LUFA;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifySchedule {

    private final TelegramClient telegramClient;
    private final ReportSubmittingService reportSubmittingService;
    private final DiscordService discordService;

    @Scheduled(cron = "0 0 21 * * *", zone = "Europe/Warsaw")
    void sendNotification() {
        reportSubmittingService.initReport()
            .doOnSuccess(ignore -> log.info("Sending notification to Telegram LufaBot about reporting today tasks"))
            .then(telegramClient.sendMessage(
                    LUFA,
                    """
                        Podaj informację nad jakimi zadaniami pracowałeś dzisiaj?
                        Rozpocznij wprowadzanie raportu podając polecenie `/start`.
                        Jeżeli chcesz pominąć ten krok, wpisz `/skip`.
                        """
                )
            )
            .then(discordService.sendClearTextMessage(
                """
                    Podaj informację nad jakimi zadaniami pracowałeś dzisiaj?
                    Rozpocznij wprowadzanie raportu podając polecenie `!start`.
                    Jeżeli chcesz pominąć ten krok, wpisz `!skip`.
                    """)
            )
            .subscribe();
    }

    @Scheduled(cron = "0 0/30 22-23 * * *", zone = "Europe/Warsaw")
    void resendNotification() {
        reportSubmittingService.shouldResendNotification()
            .filter(shouldResend -> shouldResend)
            .doOnNext(shouldResend -> log.info(
                "Resending notification to LufaBot about today tasks while it was not reported today"))
            .then(telegramClient.sendMessage(
                    LUFA,
                    """
                        Dzisiaj jeszcze nie zaraportowałeś wykonanych prac.
                        Rozpocznij wprowadzanie raportu podając polecenie `/start`.
                        Jeżeli chcesz pominąć ten krok, wpisz `/skip`.
                        """
                )
            )
            .then(discordService.sendClearTextMessage(
                """
                    Dzisiaj jeszcze nie zaraportowałeś wykonanych prac.
                    Rozpocznij wprowadzanie raportu podając polecenie `!start`.
                    Jeżeli chcesz pominąć ten krok, wpisz `!skip`.
                    """
            ))
            .subscribe();
    }
}
