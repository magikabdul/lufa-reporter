package cloud.cholewa.lufa.reporter;

import cloud.cholewa.lufa.reporter.telegram.LufaBot;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@SpringBootApplication
public class LufaReporterApplication {

    private final LufaBot lufaBot;

    public static void main(String[] args) {
        SpringApplication.run(LufaReporterApplication.class, args);


    }

    @EventListener(ApplicationReadyEvent.class)
    void startBot() {
        lufaBot.start();
    }
}
