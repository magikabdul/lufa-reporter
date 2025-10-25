package cloud.cholewa.reporter.config;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DiscordGatewayConfig {

    private final DiscordConfig discordConfig;

    @Bean
    GatewayDiscordClient lufaDiscordClient() {
        return DiscordClientBuilder.create(discordConfig.getLufa().getToken())
            .build()
            .login().block();
    }

    @Bean
    GatewayDiscordClient treDiscordClient() {
        return DiscordClientBuilder.create(discordConfig.getTre().getToken())
            .build()
            .login().block();
    }
}
