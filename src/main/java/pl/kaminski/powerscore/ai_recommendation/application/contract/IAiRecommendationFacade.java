package pl.kaminski.powerscore.ai_recommendation.application.contract;

import pl.kaminski.powerscore.ai_recommendation.domain.AiRecommendation;
import pl.kaminski.powerscore.commons.EntityId;

public interface IAiRecommendationFacade {
    AiRecommendation initializeAiRecommendation(EntityId verificationId);

}
