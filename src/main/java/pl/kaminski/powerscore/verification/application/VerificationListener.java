package pl.kaminski.powerscore.verification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.kaminski.powerscore.final_decision.application.contract.IFinalDecisionFacade;
import pl.kaminski.powerscore.final_decision.application.contract.ProcessFinalDecisionResponseCommand;
import pl.kaminski.powerscore.commons.DateTimeProvider;
import pl.kaminski.powerscore.ai_recommendation.application.contract.IAiRecommendationFacade;
import pl.kaminski.powerscore.ai_recommendation.application.contract.ProcessAiRecommendationResponseCommand;
import pl.kaminski.powerscore.open_banking.application.contract.ProcessBankingReportResponseCommand;
import pl.kaminski.powerscore.verification.domain.IVerificationRepository;

@RequiredArgsConstructor
public class VerificationListener {

    private final IVerificationRepository verificationRepository;
    private final IFinalDecisionFacade agreementFacade;
    private final DateTimeProvider dateTimeProvider;
    private final IAiRecommendationFacade aiRecommendationFacade;

    @TransactionalEventListener(ProcessBankingReportResponseCommand.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    void handleOpenBankingReportResponse(ProcessBankingReportResponseCommand message) {
        System.out.println("Received message: " + message);

        var verification = verificationRepository.findByOpenBankingReportId(message.getOpenBankingReportId())
                .orElseThrow(() -> new IllegalStateException("No verification found for report: " + message.getOpenBankingReportId()));

        verification.handleBankingReportFinished(aiRecommendationFacade);
    }

    @TransactionalEventListener(ProcessAiRecommendationResponseCommand.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    void handleAiSummaryResponse(ProcessAiRecommendationResponseCommand message) {
        System.out.println("Received message: " + message);

        var verification = verificationRepository.findByAiRecommendationId(message.getAiRecommendationId())
                .orElseThrow(() -> new IllegalStateException("No verification found for ai recommendation: " + message.getAiRecommendationId()));
        verification.handleAiSummaryFinished(agreementFacade);
        verificationRepository.save(verification);
    }

    @TransactionalEventListener(ProcessFinalDecisionResponseCommand.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    void handleFinalDecisionResponse(ProcessFinalDecisionResponseCommand message) {
        System.out.println("Received message: " + message);

        var verification = verificationRepository.findByFinalDecisionId(message.getFinalDecisionId())
                .orElseThrow(() -> new IllegalStateException("No verification found for decision: " + message.getFinalDecisionId()));

        verification.handleFinalDecisionFinished(dateTimeProvider.currentDateTime());
        verificationRepository.save(verification);
    }
}
