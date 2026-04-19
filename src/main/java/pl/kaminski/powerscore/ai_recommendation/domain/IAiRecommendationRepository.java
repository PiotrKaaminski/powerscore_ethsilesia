package pl.kaminski.powerscore.ai_recommendation.domain;

import pl.kaminski.powerscore.commons.EntityId;

import java.util.Optional;

public interface IAiRecommendationRepository {
    void save(AiRecommendation aiRecommendation);
    Optional<AiRecommendation> findById(EntityId id);
}
