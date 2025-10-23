package cloud.cholewa.reporter.report.lufa.model;

import cloud.cholewa.reporter.model.StatusType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class LufaReportStatus {

    private StatusType status;
}
