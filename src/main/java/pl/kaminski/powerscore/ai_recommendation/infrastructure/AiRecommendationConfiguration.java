package pl.kaminski.powerscore.ai_recommendation.infrastructure;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import pl.kaminski.powerscore.ai_recommendation.application.AiRecommendationFacade;
import pl.kaminski.powerscore.ai_recommendation.application.AiRecommendationListener;
import pl.kaminski.powerscore.ai_recommendation.application.contract.IAiRecommendationFacade;
import pl.kaminski.powerscore.ai_recommendation.domain.IAiRecommendationRepository;
import pl.kaminski.powerscore.client.domain.IClientRepository;
import pl.kaminski.powerscore.open_banking.domain.IOpenBankingReportRepository;
import pl.kaminski.powerscore.verification.domain.IVerificationRepository;

public class AiRecommendationConfiguration {

    private final IAiRecommendationRepository aiRecommendationRepository;

    public AiRecommendationConfiguration(AiRecommendationJpaRepository aiSummaryJpaRepository) {
        this.aiRecommendationRepository = new AiRecommendationRepository(aiSummaryJpaRepository);
    }

    @Bean
    AiRecommendationListener aiRecommendationListener(ApplicationEventPublisher applicationEventPublisher,
                                                      ChatModel chatModel, IClientRepository clientRepository,
                                                      IOpenBankingReportRepository openBankingReportRepository,
                                                      IVerificationRepository verificationRepository) {
        return new AiRecommendationListener(aiRecommendationRepository, applicationEventPublisher, chatModel, clientRepository, openBankingReportRepository, verificationRepository);
    }

    @Bean
    IAiRecommendationFacade aiRecommendationFacade(ApplicationEventPublisher applicationEventPublisher) {
        return new AiRecommendationFacade(aiRecommendationRepository, applicationEventPublisher);
    }
}
