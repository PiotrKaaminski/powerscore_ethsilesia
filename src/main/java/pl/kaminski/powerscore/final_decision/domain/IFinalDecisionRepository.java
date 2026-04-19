package pl.kaminski.powerscore.final_decision.domain;

import pl.kaminski.powerscore.commons.EntityId;
import java.util.Optional;

public interface IFinalDecisionRepository {
    FinalDecision save(FinalDecision finalDecision);
    Optional<FinalDecision> findById(EntityId id);
}
