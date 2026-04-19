package pl.kaminski.powerscore.final_decision.application.contract;

import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.final_decision.domain.FinalDecision;

public interface IFinalDecisionFacade {
    FinalDecision initializeFinalDecision();
    SendFinalDecisionResult sendFinalDecision(EntityId finalDecisionId, SendFinalDecisionRequest request);
}
