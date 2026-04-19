package pl.kaminski.powerscore.final_decision.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.final_decision.domain.FinalDecision;

@Repository
interface FinalDecisionJpaRepository extends JpaRepository<FinalDecision, EntityId> {
}
