package pl.kaminski.powerscore.ai_recommendation.application.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.kaminski.powerscore.commons.EntityId;

@Data
@AllArgsConstructor
public class ProcessAiRecommendationResponseCommand {
    private EntityId aiRecommendationId;
}
