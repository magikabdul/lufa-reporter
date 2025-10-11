package cloud.cholewa.reporter.report.tre.api;

import cloud.cholewa.reporter.report.tre.service.TreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tre")
public class TreController {

    private final TreService treService;

    @GetMapping
    Mono<ResponseEntity<List<TreResponse>>> getTreRaport(
        @RequestParam(name = "year") final int year,
        @RequestParam(name = "month") final int month
    ) {
        return treService.getReport(year, month).map(ResponseEntity::ok);
    }
}
