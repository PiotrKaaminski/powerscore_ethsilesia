package pl.kaminski.powerscore.web.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.final_decision.application.contract.IFinalDecisionFacade;
import pl.kaminski.powerscore.final_decision.application.contract.SendFinalDecisionRequest;
import pl.kaminski.powerscore.final_decision.application.contract.SendFinalDecisionResult;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FinalDecisionController {

    private final IFinalDecisionFacade finalDecisionFacade;

    @PostMapping("/finalDecision/{id}/sendDecision")
    public SendFinalDecisionResult sendFinalDecision(@PathVariable UUID id, @RequestBody SendFinalDecisionRequest request) {
        return finalDecisionFacade.sendFinalDecision(EntityId.from(id), request);
    }
}
