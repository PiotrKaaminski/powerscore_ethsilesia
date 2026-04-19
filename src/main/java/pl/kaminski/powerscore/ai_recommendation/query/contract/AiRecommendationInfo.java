package pl.kaminski.powerscore.ai_recommendation.query.contract;

import pl.kaminski.powerscore.ai_recommendation.domain.AiRecommendationStatus;

import java.util.Map;
import java.util.UUID;

public record AiRecommendationInfo(
        UUID id,
        AiRecommendationStatus status,
        Map<String, Object> recommendationData
) {
}
