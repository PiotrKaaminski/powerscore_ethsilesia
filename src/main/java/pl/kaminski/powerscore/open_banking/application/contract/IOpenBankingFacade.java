package pl.kaminski.powerscore.open_banking.application.contract;

import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.open_banking.domain.OpenBankingReport;

public interface IOpenBankingFacade {
    OpenBankingReport initializeBankingReport(InitializeBankingReportRequest request);
    StartBankingReportResult startBankingReport(EntityId openBankingId, StartBankingReportRequest request);

}
