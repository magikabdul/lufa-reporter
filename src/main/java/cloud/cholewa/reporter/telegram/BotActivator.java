package cloud.cholewa.reporter.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("reporter")
@RequiredArgsConstructor
public class BotActivator {

    private final LufaBot lufaBot;

    @EventListener(ApplicationReadyEvent.class)
    void startBot() {
        lufaBot.start();
    }
}
