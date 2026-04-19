package pl.kaminski.powerscore.ai_recommendation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.verification.domain.Verification;

import java.util.Map;

@Entity
@Table(name = "ai_recommendations")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AiRecommendation {

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "ai_recommendation_id"))
    private EntityId id;
    @Enumerated(EnumType.STRING)
    private AiRecommendationStatus status;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> recommendationData;

    public static AiRecommendation create() {
        return AiRecommendation.builder()
                .id(EntityId.newId())
                .status(AiRecommendationStatus.IN_PROGRESS)
                .build();
    }
}
