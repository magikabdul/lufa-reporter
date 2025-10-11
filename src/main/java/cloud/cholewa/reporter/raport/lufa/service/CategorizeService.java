package cloud.cholewa.reporter.raport.lufa.service;

import cloud.cholewa.reporter.raport.lufa.ai.CategorizePrompt;
import cloud.cholewa.reporter.raport.lufa.mapper.TaskChatResponseMapper;
import cloud.cholewa.reporter.raport.lufa.model.TaskCategoryChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategorizeService {

    private final ChatClient chatClient;
    private final TaskChatResponseMapper mapper;

    public Mono<TaskCategoryChatResponse> categorize(final String message) {
        return Mono.fromCallable(() -> chatClient.prompt()
                .user(message)
                .system(CategorizePrompt.PROMPT)
                .call()
                .content()
            )
            .doOnSubscribe(subscription -> log.info("Starting categorize message ..."))
            .doOnNext(response -> log.info("Response is: {}", response.replace("\n", "")))
            .map(mapper::map)
            .subscribeOn(Schedulers.boundedElastic())
            .retryWhen(Retry.max(3));
    }
}
