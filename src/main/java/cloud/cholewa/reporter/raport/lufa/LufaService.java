package cloud.cholewa.reporter.raport.lufa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
//@RequiredArgsConstructor
public class LufaService {

    private final ChatClient chatClient;

    public LufaService(final ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public Mono<String> chat() {
        return Mono.fromCallable(() -> chatClient.prompt()
                .user("Jak się masz?")
                .call()
                .content())
            .subscribeOn(Schedulers.boundedElastic());
    }
}
