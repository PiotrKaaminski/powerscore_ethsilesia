package pl.kaminski.powerscore.final_decision.query;

import jakarta.persistence.*;
import lombok.Data;
import pl.kaminski.powerscore.final_decision.domain.AgreementType;
import pl.kaminski.powerscore.final_decision.domain.FinalDecisionStatus;
import pl.kaminski.powerscore.final_decision.query.contract.FinalDecisionInfo;

import java.util.UUID;

@Entity
@Table(name = "final_decisions")
@Data
public class QFinalDecision {
    @Id
    @Column(name = "final_decision_id")
    private UUID id;
    @Enumerated(EnumType.STRING)
    private FinalDecisionStatus status;
    private String decisionData;
    @Enumerated(EnumType.STRING)
    private AgreementType agreementType;
    private Boolean isAccepted;

    public FinalDecisionInfo asDto() {
        return new FinalDecisionInfo(id, status, decisionData, agreementType, isAccepted);
    }
}

