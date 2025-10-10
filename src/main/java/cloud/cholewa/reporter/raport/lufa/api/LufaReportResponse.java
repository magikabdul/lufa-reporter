package cloud.cholewa.reporter.raport.lufa.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LufaReportResponse {
    private String category;
    private String description;
}
