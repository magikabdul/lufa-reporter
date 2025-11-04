package cloud.cholewa.reporter.report.lufa.service;

import cloud.cholewa.reporter.report.lufa.ai.ReportPrompt;
import cloud.cholewa.reporter.report.lufa.model.ReportResponse;
import cloud.cholewa.reporter.report.lufa.model.TaskCategory;
import cloud.cholewa.reporter.report.lufa.repository.LufaReportEntity;
import cloud.cholewa.reporter.report.lufa.repository.LufaReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportCreatorService {

    private final LufaReportRepository repository;
    private final ChatClient chatClient;

    public Mono<List<ReportResponse>> prepareReport(final int year, final int month) {
        return Flux.fromStream(Arrays.stream(TaskCategory.values()))
            .delayElements(Duration.ofSeconds(1))
            .concatMap(taskCategory -> fetchLufaReportSummary(year, month, taskCategory))
            .filter(reportResponse -> reportResponse.getDescription() != null)
            .collectList()
            .doOnSubscribe(subscription -> log.info("Starting report preparation ..."))
            .doOnSuccess(reports -> log.info("Report prepared"));
    }

    @NotNull
    private Mono<ReportResponse> fetchLufaReportSummary(
        final int year,
        final int month,
        final TaskCategory taskCategory
    ) {
        return repository
            .findAllByDateAndCategory(LocalDate.of(year, month, 1), taskCategory)
            .map(LufaReportEntity::getDescription)
            .collectList()
            .map(descriptions -> String.join(", ", descriptions))
            .filter(description -> !description.isEmpty())
            .flatMap(description -> synthesizeSummary(taskCategory, description))
            .map(description -> ReportResponse.builder()
                .name(taskCategory.name())
                .category(taskCategory.getDescription())
                .description(description)
                .build())
            .switchIfEmpty(Mono.just(ReportResponse.builder().build()));
    }

    private Mono<String> synthesizeSummary(final TaskCategory category, final String description) {
        return Mono.fromCallable(() -> chatClient.prompt()
                .user(description.isEmpty() ? "N/A" : description)
                .system(ReportPrompt.PROMPT)
                .call()
                .content()
            )
            .doOnSubscribe(subscription ->
                log.info("Starting synthesize summary for category: {} ...", category))
            .doOnError(error ->
                log.error("Error during API call to OpenAI: {}", error.getMessage()))
            .onErrorResume(e -> {
                log.warn("Failed to generate AI summary for category {}, using original description instead", category);
                return Mono.just("⚠️ Nie udało się wygenerować podsumowania. Oryginalne opisy: " + description);
            })
            .subscribeOn(Schedulers.boundedElastic())
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                .maxBackoff(Duration.ofSeconds(30))
                .doAfterRetry(rs ->
                    log.warn("Retry attempt {} after error connecting to OpenAI API", rs.totalRetries() + 1))
            );
    }
}
