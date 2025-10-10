package cloud.cholewa.reporter.raport.lufa.api;

import cloud.cholewa.reporter.raport.lufa.service.LufaService;
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
@RequestMapping("/lufa")
@RequiredArgsConstructor
public class LufaController {

    private final LufaService lufaService;

    @GetMapping
    Mono<ResponseEntity<List<LufaReportResponse>>> getLufaReport(@RequestParam final int year, @RequestParam final int month) {
        return lufaService.prepareReport(year, month)
            .map(ResponseEntity::ok);
    }
}
