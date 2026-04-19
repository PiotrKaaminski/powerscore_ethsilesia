package pl.kaminski.powerscore.verification.infrastructure;

import org.springframework.context.annotation.Bean;
import pl.kaminski.powerscore.final_decision.application.contract.IFinalDecisionFacade;
import pl.kaminski.powerscore.ai_recommendation.application.contract.IAiRecommendationFacade;
import pl.kaminski.powerscore.client.application.contract.IClientFacade;
import pl.kaminski.powerscore.commons.DateTimeProvider;
import pl.kaminski.powerscore.open_banking.application.contract.IOpenBankingFacade;
import pl.kaminski.powerscore.verification.application.VerificationListener;
import pl.kaminski.powerscore.verification.application.VerificationService;
import pl.kaminski.powerscore.verification.application.contract.IVerificationService;
import pl.kaminski.powerscore.verification.domain.IVerificationRepository;
import pl.kaminski.powerscore.verification.domain.VerificationFactory;
import pl.kaminski.powerscore.verification.query.IVerificationQueryRepository;
import pl.kaminski.powerscore.verification.query.VerificationQueryFacade;
import pl.kaminski.powerscore.verification.query.contract.IVerificationQueryFacade;

public class VerificationConfiguration {
    private final IVerificationRepository verificationRepository;
    private final VerificationFactory verificationFactory;
    private final IVerificationQueryRepository verificationQueryRepository;
    private final IClientFacade clientFacade;

    VerificationConfiguration(VerificationJpaRepository verificationJpaRepository,
                              DateTimeProvider dateTimeProvider,
                              VerificationQueryJpaRepository verificationQueryJpaRepository,
                              QIdentityDocumentImageJpaRepository identityDocumentImageJpaRepository,
                              IClientFacade clientFacade) {
        this.verificationRepository = new VerificationRepository(verificationJpaRepository);
        this.verificationFactory = new VerificationFactory(dateTimeProvider);
        this.verificationQueryRepository = new VerificationQueryRepository(verificationQueryJpaRepository, identityDocumentImageJpaRepository);
        this.clientFacade = clientFacade;
    }

    @Bean
    VerificationListener verificationListener(IFinalDecisionFacade agreementFacade, DateTimeProvider dateTimeProvider, IAiRecommendationFacade aiRecommendationFacade) {
        return new VerificationListener(verificationRepository, agreementFacade, dateTimeProvider, aiRecommendationFacade);
    }

    @Bean
    IVerificationService verificationService(IOpenBankingFacade openBankingFacade, IAiRecommendationFacade aiRecommendationFacade) {
        return new VerificationService(verificationRepository, verificationFactory, clientFacade, openBankingFacade, aiRecommendationFacade);
    }

    @Bean
    IVerificationQueryFacade verificationQueryFacade() {
        return new VerificationQueryFacade(verificationQueryRepository);
    }

    @Bean
    IVerificationRepository verificationRepository() {
        return verificationRepository;
    }
}
