package cloud.cholewa.reporter.report.lufa.service;

import cloud.cholewa.reporter.model.ReportingStatus;
import cloud.cholewa.reporter.report.lufa.bot.discord.DiscordService;
import cloud.cholewa.reporter.report.lufa.mapper.LufaReportEntityMapper;
import cloud.cholewa.reporter.report.lufa.model.ReportStatusResponse;
import cloud.cholewa.reporter.report.lufa.repository.LufaReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

import static cloud.cholewa.reporter.model.ReportingStatus.IN_PROGRESS;
import static cloud.cholewa.reporter.model.ReportingStatus.NOT_REPORTED;
import static cloud.cholewa.reporter.model.ReportingStatus.SKIPPED;
import static cloud.cholewa.reporter.report.lufa.service.DailyReportContext.State.CONFIRMATION;
import static cloud.cholewa.reporter.report.lufa.service.DailyReportContext.State.DESCRIPTION;
import static cloud.cholewa.reporter.report.lufa.service.DailyReportContext.State.STARTED;
import static cloud.cholewa.reporter.report.lufa.service.DailyReportContext.State.SUBMITTED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportSubmittingService {

    private final DiscordService discordService;
    private final LufaReportRepository repository;

    @NotNull
    private ReportingStatus reportingStatus = NOT_REPORTED;
    private DailyReportContext reportContext;

    public Mono<ReportStatusResponse> getDailyReportStatus() {
        return Mono.just(ReportStatusResponse.builder()
            .status(reportingStatus)
            .build());
    }

    public Mono<Void> initReport() {
        reportingStatus = NOT_REPORTED;
        reportContext = new DailyReportContext(LocalDate.now());
        return Mono.empty();
    }

    public Mono<Boolean> shouldResendNotification() {
        return Mono.just(reportingStatus != NOT_REPORTED && reportingStatus != SKIPPED);
    }

    @ReportState(requiredState = STARTED, nextState = DESCRIPTION)
    public Mono<?> startReport() {
        log.info("Report started");
        return discordService.sendClearTextMessage("Podaj opis zadnia nad którym pracowałeś dzisiaj").then();
    }

    @ReportState(requiredState = DESCRIPTION, nextState = CONFIRMATION)
    public Mono<?> addDescription(final String description) {
        log.info("Report description added: {}", description);
        reportContext.setDescription(description);
        return Mono.empty();
    }

    @ReportState(requiredState = CONFIRMATION, nextState = SUBMITTED)
    public Mono<?> confirmReport() {
        log.info("Report confirmed");
        reportingStatus = IN_PROGRESS;
        return Mono.empty();
    }


    public Mono<ReportingStatus> skipReport() {
        return Mono.fromCallable(() -> reportingStatus = SKIPPED);
    }

    public Mono<ReportingStatus> startReporting() {
        return Mono.just(reportingStatus)
            .filter(status -> status != IN_PROGRESS)
            .map(status -> IN_PROGRESS)
            .switchIfEmpty(Mono.error(() -> new IllegalStateException("Report already in progress")));
    }

    public Mono<Void> saveReport(final DailyReportContext reportContext) {
        return repository.save(LufaReportEntityMapper.mapToEntity(reportContext))
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext(entity -> log.info("Report saved to database"))
            .then();
    }

}
