package cloud.cholewa.reporter.raport.lufa.mapper;

import cloud.cholewa.reporter.raport.lufa.model.LufaReportContext;
import cloud.cholewa.reporter.raport.lufa.repository.LufaReportEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LufaReportEntityMapper {

    public static LufaReportEntity mapToEntity(final LufaReportContext reportContext) {
        LufaReportEntity entity = new LufaReportEntity();
        entity.setCreatedAt(reportContext.getCreatedDate());
        entity.setCategory(reportContext.getCategory());
        entity.setDescription(reportContext.getDescription());

        return entity;
    }
}
