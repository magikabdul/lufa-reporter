package cloud.cholewa.reporter.report.lufa.service;

import cloud.cholewa.reporter.telegram.model.StatusType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class LufaReportStatus {

    private StatusType status;
}
