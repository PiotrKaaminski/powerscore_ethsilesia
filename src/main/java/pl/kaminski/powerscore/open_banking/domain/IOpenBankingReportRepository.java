package pl.kaminski.powerscore.open_banking.domain;

import pl.kaminski.powerscore.commons.EntityId;

import java.util.Optional;

public interface IOpenBankingReportRepository {
    Optional<OpenBankingReport> findById(EntityId id);
    void save(OpenBankingReport report);
}
