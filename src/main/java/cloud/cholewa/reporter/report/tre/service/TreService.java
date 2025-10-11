package cloud.cholewa.reporter.report.tre.service;

import cloud.cholewa.reporter.report.tre.api.TreResponse;
import cloud.cholewa.reporter.report.tre.model.TreReportContext;
import cloud.cholewa.reporter.report.tre.repository.TreReportEntity;
import cloud.cholewa.reporter.report.tre.repository.TreReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class TreService {

    private final TreReportRepository treReportRepository;

    public Mono<Void> saveReport(final TreReportContext raport) {
        return Mono.just(mapToEntity(raport))
            .flatMap(treReportRepository::save)
            .doOnNext(entity -> log.info("Report saved to database"))
            .then();
    }

    private static TreReportEntity mapToEntity(final TreReportContext raport) {
        final TreReportEntity treReportEntity = new TreReportEntity();
        treReportEntity.setCreated(raport.getCreatedDate());
        treReportEntity.setCustomer(raport.getCustomer().toUpperCase(Locale.ROOT));
        treReportEntity.setDescription(StringUtils.capitalize(raport.getDescription().toLowerCase(Locale.ROOT)));
        treReportEntity.setHours(raport.getHours());
        treReportEntity.setSalesmanFirstName(StringUtils.capitalize(raport.getSalesmanFirstName().toLowerCase(Locale.ROOT)));
        treReportEntity.setSalesmanLastName(StringUtils.capitalize(raport.getSalesmanLastName().toLowerCase(Locale.ROOT)));
        if (raport.getNotes() != null) {
            treReportEntity.setNotes(StringUtils.capitalize(raport.getNotes().toLowerCase(Locale.ROOT)));
        }

        return treReportEntity;
    }

    public Mono<List<TreResponse>> getReport(final int year, final int month) {
        return treReportRepository.findAllByDate(LocalDate.of(year, month, 1))
            .doOnNext(logReportedRecord())
            .map(mapToTreResponse())
            .collectList();
    }

    @NotNull
    private static Function<TreReportEntity, TreResponse> mapToTreResponse() {
        return entity -> TreResponse.builder()
            .date(entity.getCreated().toString())
            .company(entity.getCustomer())
            .description(entity.getDescription())
            .hours(entity.getHours())
            .salesman(entity.getSalesmanLastName() + " " + entity.getSalesmanFirstName())
            .notes(entity.getNotes())
            .build();
    }

    @NotNull
    private static Consumer<TreReportEntity> logReportedRecord() {
        return entity -> log.info(
            "Report found in database: date: {}, customer: {}",
            entity.getCreated(),
            entity.getCustomer()
        );
    }
}
