package pl.kaminski.powerscore.verification.query.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.kaminski.powerscore.final_decision.query.contract.FinalDecisionInfo;
import pl.kaminski.powerscore.ai_recommendation.query.contract.AiRecommendationInfo;
import pl.kaminski.powerscore.client.query.contract.ClientInfo;
import pl.kaminski.powerscore.open_banking.query.contract.OpenBankingReportInfo;
import pl.kaminski.powerscore.verification.domain.VerificationStatus;
import pl.kaminski.powerscore.verification.domain.VerificationStepStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class VerificationInfo {
    private UUID id;
    private VerificationStatus status;
    private ClientInfo client;
    private IdentityDocumentInfo identityDocument;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private VerificationStepStatus kycVerificationStatus;
    private VerificationStepStatus bankVerificationStatus;
    private VerificationStepStatus aiRecommendationStatus;
    private VerificationStepStatus finalSummaryStatus;
    private Boolean bankVerificationApproved;
    private OpenBankingReportInfo openBankingReport;
    private AiRecommendationInfo aiRecommendation;
    private FinalDecisionInfo finalDecision;
}
