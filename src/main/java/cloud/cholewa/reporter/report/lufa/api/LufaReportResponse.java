package cloud.cholewa.reporter.report.lufa.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LufaReportResponse {
    private String category;
    private String description;
}
