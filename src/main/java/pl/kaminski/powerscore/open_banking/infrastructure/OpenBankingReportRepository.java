package pl.kaminski.powerscore.open_banking.infrastructure;

import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.open_banking.domain.IOpenBankingReportRepository;
import pl.kaminski.powerscore.open_banking.domain.OpenBankingReport;

import java.util.Optional;

@RequiredArgsConstructor
public class OpenBankingReportRepository implements IOpenBankingReportRepository {

    private final OpenBankingReportJpaRepository jpaRepository;

    @Override
    public Optional<OpenBankingReport> findById(EntityId id) {
        return jpaRepository.findById(id);
    }

    public void save(OpenBankingReport report) {
        jpaRepository.save(report);
    }

}
