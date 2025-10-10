package cloud.cholewa.reporter.raport.lufa.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface LufaReportRepository extends R2dbcRepository<LufaReportEntity, Long> {
}
