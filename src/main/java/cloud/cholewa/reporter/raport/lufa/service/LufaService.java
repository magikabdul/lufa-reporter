package cloud.cholewa.reporter.raport.lufa.service;

import cloud.cholewa.reporter.raport.lufa.api.LufaReportResponse;
import cloud.cholewa.reporter.raport.lufa.mapper.LufaReportEntityMapper;
import cloud.cholewa.reporter.raport.lufa.model.LufaReportContext;
import cloud.cholewa.reporter.raport.lufa.model.TaskCategory;
import cloud.cholewa.reporter.raport.lufa.repository.LufaReportEntity;
import cloud.cholewa.reporter.raport.lufa.repository.LufaReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LufaService {

    private final LufaReportRepository repository;
    private final ChatClient chatClient;


    public Mono<Void> saveReport(final LufaReportContext reportContext) {
        return repository.save(LufaReportEntityMapper.mapToEntity(reportContext))
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext(entity -> log.info("Report saved to database"))
            .then();
    }

    public Mono<List<LufaReportResponse>> prepareReport(final int year, final int month) {
        return Flux.fromStream(Arrays.stream(TaskCategory.values()))
            .flatMap(taskCategory -> repository
                .findAllByDateAndCategory(LocalDate.of(year, month, 1), taskCategory)
                .map(LufaReportEntity::getDescription)
                .collectList()
                .map(descriptions -> String.join(", ", descriptions))
                .map(description -> LufaReportResponse.builder()
                    .category(taskCategory.getDescription())
                    .description(description)
                    .build()))
            .collectList();
    }
}
