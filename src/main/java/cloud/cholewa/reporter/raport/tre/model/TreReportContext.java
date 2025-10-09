package cloud.cholewa.reporter.raport.tre.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class TreReportContext {

    private final LocalDate createdDate;

    private String customer;
    private String description;
    private int hours;
    private String salesmanFirstName;
    private String salesmanLastName;
    private String notes;
}
