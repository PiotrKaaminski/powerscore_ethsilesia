package pl.kaminski.powerscore.ai_recommendation.infrastructure;

import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.ai_recommendation.domain.AiRecommendation;
import pl.kaminski.powerscore.ai_recommendation.domain.IAiRecommendationRepository;
import pl.kaminski.powerscore.commons.EntityId;

import java.util.Optional;

@RequiredArgsConstructor
public class AiRecommendationRepository implements IAiRecommendationRepository {

    private final AiRecommendationJpaRepository aiRecommendationJpaRepository;

    @Override
    public void save(AiRecommendation aiRecommendation) {
        aiRecommendationJpaRepository.save(aiRecommendation);
    }

    @Override
    public Optional<AiRecommendation> findById(EntityId id) {
        return aiRecommendationJpaRepository.findById(id);

    }
}
