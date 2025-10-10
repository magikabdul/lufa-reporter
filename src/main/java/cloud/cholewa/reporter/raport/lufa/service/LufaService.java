package cloud.cholewa.reporter.raport.lufa.service;

import cloud.cholewa.reporter.raport.lufa.mapper.LufaReportEntityMapper;
import cloud.cholewa.reporter.raport.lufa.model.LufaReportContext;
import cloud.cholewa.reporter.raport.lufa.repository.LufaReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class LufaService {

    private final LufaReportRepository repository;

    public Mono<Void> saveReport(final LufaReportContext reportContext) {
        return repository.save(LufaReportEntityMapper.mapToEntity(reportContext))
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext(entity -> log.info("Report saved to database"))
            .then();
    }
}
