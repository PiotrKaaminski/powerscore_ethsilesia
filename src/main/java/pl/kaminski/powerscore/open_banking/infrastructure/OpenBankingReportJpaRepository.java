package pl.kaminski.powerscore.open_banking.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.open_banking.domain.OpenBankingReport;

@Repository
public interface OpenBankingReportJpaRepository extends JpaRepository<OpenBankingReport, EntityId> {
}
