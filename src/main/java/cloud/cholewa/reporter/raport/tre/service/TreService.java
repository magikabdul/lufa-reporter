package cloud.cholewa.reporter.raport.tre.service;

import cloud.cholewa.reporter.raport.tre.api.TreResponse;
import cloud.cholewa.reporter.raport.tre.model.TreRaportContext;
import cloud.cholewa.reporter.raport.tre.repository.TreRaportEntity;
import cloud.cholewa.reporter.raport.tre.repository.TreRaportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class TreService {

    private final TreRaportRepository treRaportRepository;

    public Mono<Void> saveReport(final TreRaportContext raport) {
        return Mono.just(mapToEntity(raport))
            .flatMap(treRaportRepository::save)
            .doOnNext(entity -> log.info("Report saved to database"))
            .then();
    }

    private static TreRaportEntity mapToEntity(final TreRaportContext raport) {
        final TreRaportEntity treRaportEntity = new TreRaportEntity();
        treRaportEntity.setCreated(raport.getCreatedDate());
        treRaportEntity.setCustomer(raport.getCustomer().toUpperCase(Locale.ROOT));
        treRaportEntity.setDescription(StringUtils.capitalize(raport.getDescription().toLowerCase(Locale.ROOT)));
        treRaportEntity.setHours(raport.getHours());
        treRaportEntity.setSalesmanFirstName(StringUtils.capitalize(raport.getSalesmanFirstName().toLowerCase(Locale.ROOT)));
        treRaportEntity.setSalesmanLastName(StringUtils.capitalize(raport.getSalesmanLastName().toLowerCase(Locale.ROOT)));
        if (raport.getNotes() != null) {
            treRaportEntity.setNotes(StringUtils.capitalize(raport.getNotes().toLowerCase(Locale.ROOT)));
        }

        return treRaportEntity;
    }

    public Mono<List<TreResponse>> getReport(final int year, final int month) {
        return treRaportRepository.findAllByDate(LocalDate.of(year, month, 1))
            .doOnNext(entity -> log.info("Report found in database: {}", entity))
            .map(entity -> TreResponse.builder()
                .date(entity.getCreated().toString())
                .company(entity.getCustomer())
                .description(entity.getDescription())
                .hours(entity.getHours())
                .salesman(entity.getSalesmanLastName() + " " + entity.getSalesmanFirstName())
                .notes(entity.getNotes())
                .build())
            .collectList();
    }
}
