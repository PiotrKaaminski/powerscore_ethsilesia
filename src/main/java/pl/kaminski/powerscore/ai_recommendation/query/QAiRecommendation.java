package pl.kaminski.powerscore.ai_recommendation.query;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.kaminski.powerscore.ai_recommendation.domain.AiRecommendationStatus;
import pl.kaminski.powerscore.ai_recommendation.query.contract.AiRecommendationInfo;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "ai_recommendations")
@Data
public class QAiRecommendation {

    @Id
    @Column(name = "ai_recommendation_id")
    private UUID id;
    @Enumerated(EnumType.STRING)
    private AiRecommendationStatus status;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> recommendationData;

    public AiRecommendationInfo asDto() {
        return new AiRecommendationInfo(id, status, recommendationData);
    }
}
