package cloud.cholewa.reporter.report.lufa.bot;

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
@SuppressWarnings("unused")
public class LufaBot {

    private final TelegramConfig telegramConfig;
    private final LufaMessageHandler lufaMessageHandler;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.debug("Lufa bot started and waiting for commands");

        final BotClient lufaBot = new BotClient(telegramConfig.getLufa().getToken());
        lufaBot.addHandler(lufaMessageHandler);
        lufaBot.startPolling();
    }
}
