package cloud.cholewa.reporter.report.lufa.model;

import cloud.cholewa.reporter.model.ReportingStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class LufaReportStatus {

    private ReportingStatus status;
}
