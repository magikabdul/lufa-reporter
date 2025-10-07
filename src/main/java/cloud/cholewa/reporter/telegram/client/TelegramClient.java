package cloud.cholewa.reporter.telegram.client;

import cloud.cholewa.reporter.config.TelegramConfig;
import cloud.cholewa.reporter.telegram.model.VendorName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static cloud.cholewa.reporter.telegram.model.VendorName.LUFA;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramClient {

    private final WebClient telegramWebClient;
    private final TelegramConfig telegramConfig;

    public Mono<Void> sendMessage(final VendorName vendorName, final String message) {
        return telegramWebClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("bot" + getToken(vendorName))
                .pathSegment("sendMessage")
                .queryParam("chat_id", getChatId(vendorName))
                .queryParam("text", message)
                .build())
            .retrieve()
            .bodyToMono(Void.class)
            .doOnSuccess(empty -> log.info("Message sent with content: {}", message.split("\n")[0]));
    }

    private String getChatId(final VendorName vendorName) {
        return vendorName.equals(LUFA)
            ? telegramConfig.getLufa().getChatId()
            : telegramConfig.getTre().getChatId();
    }

    private String getToken(final VendorName vendorName) {
        return vendorName.equals(LUFA)
            ? telegramConfig.getLufa().getToken()
            : telegramConfig.getTre().getToken();
    }
}
