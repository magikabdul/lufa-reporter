package cloud.cholewa.reporter.report.tre.bot;

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
public class TreBot {

    private final TelegramConfig telegramConfig;
    private final MessageProcessor messageProcessor;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.debug("Tre bot started and waiting for commands");

        final BotClient treBot = new BotClient(telegramConfig.getTre().getToken());
        treBot.addHandler(messageProcessor);
        treBot.startPolling();
        treBot.context.clearState(Long.parseLong(telegramConfig.getTre().getChatId()));
    }
}
