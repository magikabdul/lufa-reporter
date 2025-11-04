package cloud.cholewa.reporter.report.lufa.api;

import cloud.cholewa.reporter.report.lufa.model.ReportResponse;
import cloud.cholewa.reporter.report.lufa.model.ReportStatusResponse;
import cloud.cholewa.reporter.report.lufa.service.ReportCreatorService;
import cloud.cholewa.reporter.report.lufa.service.ReportSubmittingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lufa")
@RequiredArgsConstructor
public class LufaController {

    private final ReportSubmittingService reportSubmittingService;
    private final ReportCreatorService reportCreatorService;

    @GetMapping
    Mono<ResponseEntity<List<ReportResponse>>> getLufaReport(
        @RequestParam final int year,
        @RequestParam final int month
    ) {
        return reportCreatorService.prepareReport(year, month)
            .filter(reports -> !reports.isEmpty())
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.OK)
                .body(List.of(ReportResponse.builder()
                    .description("No records in data base for: " + year + "-" + month)
                    .build()))
            ));
    }

    @GetMapping("status")
    Mono<ResponseEntity<ReportStatusResponse>> getReportDailyStatus() {
        return reportSubmittingService.getDailyReportStatus()
            .doOnNext(status -> log.info("Report status: {}", status))
            .map(ResponseEntity::ok);
    }
}
