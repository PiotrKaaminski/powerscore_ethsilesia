package pl.kaminski.powerscore.final_decision.infrastructure;

import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.final_decision.domain.IFinalDecisionRepository;
import pl.kaminski.powerscore.final_decision.domain.FinalDecision;

import java.util.Optional;

@RequiredArgsConstructor
class FinalDecisionRepository implements IFinalDecisionRepository {

    private final FinalDecisionJpaRepository jpaRepository;

    @Override
    public Optional<FinalDecision> findById(EntityId id) {
        return jpaRepository.findById(id);
    }

    @Override
    public FinalDecision save(FinalDecision finalDecision) {
        return jpaRepository.save(finalDecision);
    }

}
