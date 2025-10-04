package cloud.cholewa.reporter.raport.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotifySchedule {

    @Scheduled(fixedDelay = 10000)
    void sendNotification() {
        log.info("Notifying");
    }
}
