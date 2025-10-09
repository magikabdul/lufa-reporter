package cloud.cholewa.reporter.raport.tre.repository;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
@Table("tre")
public class TreReportEntity {

    @Id
    private Long id;

    @Column("created_at")
    @NotNull
    private LocalDate created;

    @NotNull
    @Column("customer_name")
    private String customer;

    @NotNull
    private String description;

    @NotNull
    @Min(1)
    @Column("hours_spent")
    private int hours;

    @NotNull
    @Column("salesperson_first_name")
    private String salesmanFirstName;

    @NotNull
    @Column("salesperson_last_name")
    private String salesmanLastName;

    @Nullable
    private String notes;
}
