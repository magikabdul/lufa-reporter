package cloud.cholewa.reporter.raport.lufa.repository;

import cloud.cholewa.reporter.raport.lufa.model.TaskCategory;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("lufa")
public class LufaReportEntity {

    @Id
    private Long id;

    private LocalDate createdAt;

    private TaskCategory category;

    private String description;
}
