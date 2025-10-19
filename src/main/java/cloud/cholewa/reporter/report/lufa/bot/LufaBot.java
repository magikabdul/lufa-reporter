package cloud.cholewa.reporter.report.lufa.bot;

import cloud.cholewa.reporter.config.TelegramConfig;
import io.github.natanimn.telebof.BotClient;
import io.github.natanimn.telebof.BotLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

@Slf4j
@Component
@Profile("reporter")
@RequiredArgsConstructor
public class LufaBot {

    private final TelegramConfig telegramConfig;
    private final LufaMessageHandler lufaMessageHandler;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.debug("Lufa bot started and waiting for commands");
        BotLog.setLogLevel(Level.ALL);
        final BotClient lufaBot = new BotClient(telegramConfig.getLufa().getToken());
        lufaBot.addHandler(lufaMessageHandler);
        lufaBot.startPolling();
        lufaBot.context.clearState(Long.parseLong(telegramConfig.getLufa().getChatId()));
    }
}
