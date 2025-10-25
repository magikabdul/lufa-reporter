package cloud.cholewa.reporter.report.lufa.service;

import cloud.cholewa.reporter.model.ReportingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static cloud.cholewa.reporter.model.ReportingStatus.NOT_REPORTED;
import static cloud.cholewa.reporter.model.ReportingStatus.SKIPPED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportHandlerService {

    @NotNull
    private ReportingStatus reportingStatus = NOT_REPORTED;
    private DailyReportContext reportContext;

    public Mono<Void> initReport() {
        reportingStatus = NOT_REPORTED;
        reportContext = new DailyReportContext(LocalDate.now());
        return Mono.empty();
    }

    public Mono<ReportingStatus> getDailyReportStatus () {
        return Mono.just(reportingStatus);
    }

    public Mono<Boolean> shouldResendNotification() {
        return Mono.just(reportingStatus != NOT_REPORTED && reportingStatus != SKIPPED);
    }
}
