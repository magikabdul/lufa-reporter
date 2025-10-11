package cloud.cholewa.reporter.report.tre.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface TreReportRepository extends R2dbcRepository<TreReportEntity, Long> {

    @Query("SELECT * FROM tre WHERE created_at >= DATE_TRUNC('month', :date) AND created_at < DATE_TRUNC('month', :date) + INTERVAL '1 month'")
    Flux<TreReportEntity> findAllByDate(LocalDate date);
}
