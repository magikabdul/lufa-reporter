package cloud.cholewa.reporter.raport.lufa.api;

import cloud.cholewa.reporter.raport.lufa.service.LufaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/lufa")
@RequiredArgsConstructor
public class LufaController {

    private final LufaService lufaService;

}
