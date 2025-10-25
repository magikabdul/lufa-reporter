package cloud.cholewa.reporter.report.tre.status;

import cloud.cholewa.reporter.model.ReportingStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class TreStatus {

    private ReportingStatus status;
}
