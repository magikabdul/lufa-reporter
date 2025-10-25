package cloud.cholewa.reporter.report.lufa.service;

import cloud.cholewa.reporter.report.lufa.model.TaskCategory;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
class DailyReportContext {

    private final LocalDate createdDate;
    private TaskCategory category;
    private String description;
}
