package cloud.cholewa.reporter.report.lufa.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class LufaReportContext {

    private final LocalDate createdDate;
    private TaskCategory category;
    private String description;
}
