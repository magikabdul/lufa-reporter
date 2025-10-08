package cloud.cholewa.reporter.raport.tre.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreResponse {

    private String company;
    private String description;
    private int hours;
    private String salesman;
    private String notes;
}
