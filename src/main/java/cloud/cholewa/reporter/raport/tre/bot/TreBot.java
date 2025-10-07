package cloud.cholewa.reporter.raport.tre.bot;

import cloud.cholewa.reporter.config.TelegramConfig;
import io.github.natanimn.telebof.BotClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("reporter")
@RequiredArgsConstructor
public class TreBot {

    private final TelegramConfig telegramConfig;
    private final TreMessageHandler treMessageHandler;

    @EventListener(ApplicationReadyEvent.class)
    void start() {
        log.info("Tre bot started and waiting for commands");

        final BotClient treBot = new BotClient(telegramConfig.getTre().getToken());
        treBot.addHandler(treMessageHandler);
        treBot.startPolling();
    }
}
