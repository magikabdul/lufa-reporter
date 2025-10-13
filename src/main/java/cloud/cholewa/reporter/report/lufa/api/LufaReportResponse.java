package cloud.cholewa.reporter.report.lufa.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LufaReportResponse {
    private String name;
    private String category;
    private String description;
}
