package pl.kaminski.powerscore.final_decision.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.final_decision.application.contract.IFinalDecisionFacade;
import pl.kaminski.powerscore.final_decision.application.contract.SendFinalDecisionRequest;
import pl.kaminski.powerscore.final_decision.application.contract.SendFinalDecisionResult;
import pl.kaminski.powerscore.final_decision.domain.IFinalDecisionRepository;
import pl.kaminski.powerscore.final_decision.domain.FinalDecision;

@RequiredArgsConstructor
public class FinalDecisionFacade implements IFinalDecisionFacade {

    private final IFinalDecisionRepository finalDecisionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public FinalDecision initializeFinalDecision() {
        var finalDecision = FinalDecision.create();
        finalDecisionRepository.save(finalDecision);
        return finalDecision;
    }

    @Override
    @Transactional
    public SendFinalDecisionResult sendFinalDecision(EntityId finalDecisionId, SendFinalDecisionRequest request) {
        if (request == null || request.getIsAccepted() == null || (request.getIsAccepted() && request.getAgreementType() == null)) {
            return SendFinalDecisionResult.invalidInputData();
        }
        var finalDecisionOptional = finalDecisionRepository.findById(finalDecisionId);
        if (finalDecisionOptional.isEmpty()) {
            return SendFinalDecisionResult.finalDecisionNotFound();
        }
        var finalDecision = finalDecisionOptional.get();
        var result = finalDecision.sendFinalDecision(request);
        if (result.isError()) {
            return result;
        }
        for (var event : finalDecision.getEvents()) {
            applicationEventPublisher.publishEvent(event);
        }
        return result;
    }
}
