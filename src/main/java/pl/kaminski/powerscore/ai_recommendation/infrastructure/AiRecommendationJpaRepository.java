package pl.kaminski.powerscore.ai_recommendation.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kaminski.powerscore.ai_recommendation.domain.AiRecommendation;
import pl.kaminski.powerscore.commons.EntityId;

@Repository
public interface AiRecommendationJpaRepository extends JpaRepository<AiRecommendation, EntityId> {
}
