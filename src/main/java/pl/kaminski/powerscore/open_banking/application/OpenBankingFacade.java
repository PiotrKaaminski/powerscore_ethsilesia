package pl.kaminski.powerscore.open_banking.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.open_banking.application.contract.*;
import pl.kaminski.powerscore.open_banking.domain.IOpenBankingReportRepository;
import pl.kaminski.powerscore.open_banking.domain.OpenBankingReport;

@RequiredArgsConstructor
@Slf4j
public class OpenBankingFacade implements IOpenBankingFacade {

    private final IOpenBankingReportRepository openBankingReportRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final KontomatikClient kontomatikClient;

    @Override
    @Transactional
    public OpenBankingReport initializeBankingReport(InitializeBankingReportRequest request) {
        var openBankingReport = OpenBankingReport.create();
        openBankingReportRepository.save(openBankingReport);

//        applicationEventPublisher.publishEvent(new RequestBankingReportCommand(openBankingReport.getId()));
        return openBankingReport;
    }

    @Override
    @Transactional
    public StartBankingReportResult startBankingReport(EntityId openBankingId, StartBankingReportRequest request) {
        if (request.getSessionId() == null || request.getSessionIdSignature() == null || request.getOwnerExternalId() == null) {
            return StartBankingReportResult.invalidInputData();
        }

        var openBankingReportOptional = openBankingReportRepository.findById(openBankingId);
        if (openBankingReportOptional.isEmpty()) {
            return StartBankingReportResult.openBankingReportNotFound();
        }
        String commandId = kontomatikClient.defaultImport(request.getSessionId(), request.getSessionIdSignature());

        var openBankingReport = openBankingReportOptional.get();
        openBankingReport.startBankingReport(request, commandId);


        applicationEventPublisher.publishEvent(new RequestBankingReportCommand(openBankingReport.getId()));

        return StartBankingReportResult.success();
    }
}
