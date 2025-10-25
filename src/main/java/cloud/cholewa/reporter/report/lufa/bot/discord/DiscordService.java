package cloud.cholewa.reporter.report.lufa.bot.discord;

import cloud.cholewa.reporter.config.DiscordConfig;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordService {

    private final DiscordConfig discordConfig;
    private final GatewayDiscordClient lufaDiscordClient;

    public Mono<Message> sendClearTextMessage(final String message) {
        return lufaDiscordClient.getChannelById(Snowflake.of(discordConfig.getLufa().getChannelId()))
            .ofType(MessageChannel.class)
            .flatMap(channel -> channel.createMessage(message))
            .doOnSuccess(success -> log.info("Clear text message sent to Discord"));
    }
}
