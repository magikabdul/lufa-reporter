package cloud.cholewa.reporter.infrastructure.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionMessage {

    private String code;
    private String title;
    private String detail;

}
