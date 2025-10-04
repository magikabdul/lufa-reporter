package cloud.cholewa.reporter.telegram;

import cloud.cholewa.reporter.config.TelegramConfig;
import io.github.natanimn.telebof.BotClient;
import io.github.natanimn.telebof.filters.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LufaBot {

    private final TelegramConfig telegramConfig;

    public void start() {
        final BotClient bot = new BotClient(telegramConfig.getLufa().getToken());

        log.info("Bot started");

        bot.onMessage(
            Filter::text,
            ((botContext, message) ->
                botContext.sendMessage(message.chat.id, "You said: " + message.text).exec())
        );

        bot.startPolling();
    }
}
