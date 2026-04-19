package pl.kaminski.powerscore.ai_recommendation.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import pl.kaminski.powerscore.ai_recommendation.application.contract.IAiRecommendationFacade;
import pl.kaminski.powerscore.ai_recommendation.application.contract.RequestAiRecommendationCommand;
import pl.kaminski.powerscore.ai_recommendation.domain.AiRecommendation;
import pl.kaminski.powerscore.ai_recommendation.domain.IAiRecommendationRepository;
import pl.kaminski.powerscore.commons.EntityId;

@RequiredArgsConstructor
public class AiRecommendationFacade implements IAiRecommendationFacade {

    private final IAiRecommendationRepository aiRecommendationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public AiRecommendation initializeAiRecommendation(EntityId verificationId) {
        var aiRecommendation = AiRecommendation.create();

        aiRecommendationRepository.save(aiRecommendation);
        applicationEventPublisher.publishEvent(new RequestAiRecommendationCommand(aiRecommendation.getId(), verificationId));
        return aiRecommendation;
    }
}
