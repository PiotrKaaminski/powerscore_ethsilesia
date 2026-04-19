package pl.kaminski.powerscore.final_decision.application.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.kaminski.powerscore.commons.EntityId;

@Data
@AllArgsConstructor
public class ProcessFinalDecisionResponseCommand {
    private EntityId finalDecisionId;
}
