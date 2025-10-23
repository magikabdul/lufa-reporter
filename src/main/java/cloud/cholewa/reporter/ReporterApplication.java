package cloud.cholewa.reporter;

import cloud.cholewa.reporter.config.DiscordConfig;
import cloud.cholewa.reporter.config.TelegramConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@EnableConfigurationProperties({TelegramConfig.class, DiscordConfig.class})
@SpringBootApplication
public class ReporterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReporterApplication.class, args);
    }
}
