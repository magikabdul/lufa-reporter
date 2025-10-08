package cloud.cholewa.reporter.raport.tre.status;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class TreStatus {

    private TreStatusType status;

    public enum TreStatusType {
        REPORTED,
        NOT_REPORTED,
        IN_PROGRESS,
        SKIPPED
    }
}
