package cloud.cholewa.reporter.raport.lufa.api;

import cloud.cholewa.reporter.raport.lufa.LufaService;
import cloud.cholewa.reporter.raport.lufa.model.TaskChatResponse;
import cloud.cholewa.reporter.raport.lufa.service.CategorizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/lufa")
@RequiredArgsConstructor
public class LufaController {

    private final LufaService lufaService;
    private final CategorizeService categorizeService;

    @GetMapping
//    Mono<ResponseEntity<String>> getLufaRaport() {
//        return lufaService.chat()
//            .map(ResponseEntity::ok);
//    }
    Mono<ResponseEntity<TaskChatResponse>> getCategory() {
        return categorizeService.categorize("Recenzja zadania związanego z nagłówkiem identyfikacyjnym")
            .map(ResponseEntity::ok);
    }
}
