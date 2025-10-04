package cloud.cholewa.reporter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "telegram")
public class TelegramConfig {

    private String url;
    private Vendor lufa;
    private Vendor tre;

    @Getter
    @Setter
    public static class Vendor {
        private String token;
        private String chatId;
    }
}
