package cloud.cholewa.reporter.report.lufa.repository;

import cloud.cholewa.reporter.report.lufa.model.TaskCategory;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface LufaReportRepository extends R2dbcRepository<LufaReportEntity, Long> {

    @Query("SELECT * FROM lufa WHERE category= :category AND created_at >= DATE_TRUNC('month', :date) AND created_at < DATE_TRUNC('month', :date) + INTERVAL '1 month'")
    Flux<LufaReportEntity> findAllByDateAndCategory(LocalDate date, TaskCategory category);
}
