package cloud.cholewa.reporter.config;

import cloud.cholewa.reporter.infrastructure.error.GlobalErrorWebExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.IGNORE_UNKNOWN;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final TelegramConfig telegramConfig;

    @Bean
    ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(IGNORE_UNKNOWN, true);

        return objectMapper;
    }

    @Bean
    WebClient telegramWebClient() {
        return WebClient.create(telegramConfig.getUrl());
    }

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    @Order(-2)
    GlobalErrorWebExceptionHandler globalErrorWebExceptionHandler(
        final ErrorAttributes errorAttributes,
        final WebProperties webProperties,
        final ApplicationContext applicationContext,
        final ServerCodecConfigurer serverCodecConfigurer
    ) {
        return new GlobalErrorWebExceptionHandler(
            errorAttributes, webProperties.getResources(), applicationContext, serverCodecConfigurer);
    }
}
