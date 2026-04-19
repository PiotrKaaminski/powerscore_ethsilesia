package pl.kaminski.powerscore.final_decision.domain;

import jakarta.persistence.*;
import lombok.*;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.final_decision.application.contract.ProcessFinalDecisionResponseCommand;
import pl.kaminski.powerscore.final_decision.application.contract.SendFinalDecisionRequest;
import pl.kaminski.powerscore.final_decision.application.contract.SendFinalDecisionResult;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "final_decisions")
@Data
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class FinalDecision {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "final_decision_id"))
    private EntityId id;
    @Enumerated(EnumType.STRING)
    private FinalDecisionStatus status;
    @Enumerated(EnumType.STRING)
    private AgreementType agreementType;
    private Boolean isAccepted;
    private String decisionData;

    @Transient
    private final List<Object> events = new ArrayList<>();

    public SendFinalDecisionResult sendFinalDecision(SendFinalDecisionRequest request) {
        if (status != FinalDecisionStatus.IN_PROGRESS) {
            return SendFinalDecisionResult.finalDecisionAlreadySent();
        }
        isAccepted = request.getIsAccepted();
        agreementType = request.getAgreementType();
        decisionData = request.getDecisionData();
        status = FinalDecisionStatus.FINISHED;
        events.add(new ProcessFinalDecisionResponseCommand(id));
        return SendFinalDecisionResult.success();
    }

    public static FinalDecision create() {
        return FinalDecision.builder()
                .id(EntityId.newId())
                .status(FinalDecisionStatus.IN_PROGRESS)
                .build();
    }
}
