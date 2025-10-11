package cloud.cholewa.reporter.report.tre.status;

import cloud.cholewa.reporter.telegram.model.StatusType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class TreStatus {

    private StatusType status;
}
