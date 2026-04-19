package pl.kaminski.powerscore.final_decision.query.contract;

import pl.kaminski.powerscore.final_decision.domain.AgreementType;
import pl.kaminski.powerscore.final_decision.domain.FinalDecisionStatus;

import java.util.UUID;

public record FinalDecisionInfo(
        UUID id,
        FinalDecisionStatus status,
        String decisionData,
        AgreementType agreementType,
        Boolean isAccepted
) {
}
