package cloud.cholewa.reporter.report.lufa.api;

import cloud.cholewa.reporter.model.ReportingStatus;
import cloud.cholewa.reporter.report.lufa.service.ReportHandlerService;
import cloud.cholewa.reporter.report.lufa.service.ReportService;
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

    private final ReportService reportService;
    private final ReportHandlerService reportHandlerService;

    @GetMapping
    Mono<ResponseEntity<List<LufaReportResponse>>> getLufaReport(
        @RequestParam final int year,
        @RequestParam final int month
    ) {
        return reportService.prepareReport(year, month)
            .filter(reports -> !reports.isEmpty())
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.OK)
                .body(List.of(LufaReportResponse.builder()
                    .description("No records in data base for: " + year + "-" + month)
                    .build()))
            ));
    }

    @GetMapping("status")
    Mono<ResponseEntity<ReportingStatus>> getReportDailyStatus() {
        return reportHandlerService.getDailyReportStatus()
            .doOnNext(status -> log.info("Report status: {}", status))
            .map(ResponseEntity::ok);
    }
}
