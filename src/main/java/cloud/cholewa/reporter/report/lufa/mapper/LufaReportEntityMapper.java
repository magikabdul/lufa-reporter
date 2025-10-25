package cloud.cholewa.reporter.report.lufa.mapper;

import cloud.cholewa.reporter.report.lufa.service.DailyReportContext;
import cloud.cholewa.reporter.report.lufa.repository.LufaReportEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LufaReportEntityMapper {

    public static LufaReportEntity mapToEntity(final DailyReportContext reportContext) {
        LufaReportEntity entity = new LufaReportEntity();
        entity.setCreatedAt(reportContext.getCreatedDate());
        entity.setCategory(reportContext.getCategory());
        entity.setDescription(reportContext.getDescription());

        return entity;
    }
}
