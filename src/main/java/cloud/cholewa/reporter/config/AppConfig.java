package cloud.cholewa.reporter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final TelegramConfig telegramConfig;

    @Bean
    WebClient telegramWebClient() {
        return WebClient.create(telegramConfig.getUrl());
    }
}
