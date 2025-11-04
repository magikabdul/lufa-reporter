package cloud.cholewa.reporter.report.lufa.model;

import cloud.cholewa.reporter.model.ReportingStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportStatusResponse {
    private ReportingStatus status;
}
