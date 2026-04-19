package pl.kaminski.powerscore.final_decision.application.contract;

import lombok.Data;
import pl.kaminski.powerscore.final_decision.domain.AgreementType;

@Data
public class SendFinalDecisionRequest {
    private AgreementType agreementType;
    private Boolean isAccepted;
    private String decisionData;
}
