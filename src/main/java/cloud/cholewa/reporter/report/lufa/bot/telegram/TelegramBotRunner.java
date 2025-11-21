package cloud.cholewa.reporter.report.lufa.bot.telegram;

import cloud.cholewa.reporter.config.TelegramConfig;
import io.github.natanimn.telebof.BotClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("reporter")
@RequiredArgsConstructor
public class TelegramBotRunner {

    private final TelegramConfig telegramConfig;
    private final TelegramMessageProcessor telegramMessageProcessor;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.debug("Lufa Telegram bot started and is waiting for commands");
        final BotClient lufaBot = new BotClient(telegramConfig.getLufa().getToken());
        lufaBot.addHandler(telegramMessageProcessor);
        lufaBot.startPolling();
        lufaBot.context.clearState(Long.parseLong(telegramConfig.getLufa().getChatId()));
    }
}
