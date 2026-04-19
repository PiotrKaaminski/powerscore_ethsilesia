package pl.kaminski.powerscore.verification.domain;

import jakarta.persistence.*;
import lombok.*;
import pl.kaminski.powerscore.final_decision.application.contract.IFinalDecisionFacade;
import pl.kaminski.powerscore.ai_recommendation.application.contract.IAiRecommendationFacade;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.open_banking.application.contract.IOpenBankingFacade;
import pl.kaminski.powerscore.open_banking.application.contract.InitializeBankingReportRequest;
import pl.kaminski.powerscore.verification.application.contract.HandleBankingReportApprovalRequest;
import pl.kaminski.powerscore.verification.application.contract.HandleBankingReportApprovalResult;
import pl.kaminski.powerscore.verification.application.contract.UploadDocumentImageResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "verifications")
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Verification {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "verification_id"))
    private EntityId id;
    @Enumerated(EnumType.STRING)
    private VerificationStatus status;
    @AttributeOverride(name = "value", column = @Column(name = "client_id"))
    private EntityId clientId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "identity_document_id")
    private IdentityDocument identityDocument;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    @Enumerated(EnumType.STRING)
    private VerificationStepStatus kycVerificationStatus;
    @Enumerated(EnumType.STRING)
    private VerificationStepStatus bankVerificationStatus;
    private Boolean bankVerificationApproved;
    @Enumerated(EnumType.STRING)
    private VerificationStepStatus aiRecommendationStatus;
    @Enumerated(EnumType.STRING)
    private VerificationStepStatus finalSummaryStatus;
    @AttributeOverride(name = "value", column = @Column(name = "open_banking_report_id"))
    private EntityId openBankingReportId;
    @AttributeOverride(name = "value", column = @Column(name = "ai_recommendation_id"))
    private EntityId aiRecommendationId;
    @AttributeOverride(name = "value", column = @Column(name = "final_decision_id"))
    private EntityId finalDecisionId;

    @Transient
    private final List<Object> events = new ArrayList<>();

    public UploadDocumentImageResult uploadDocumentImage(byte[] documentImage, String contentType) {
        return identityDocument.uploadDocumentImage(documentImage, contentType);
    }

    public void checkKycVerificationFinished() {
        if (clientId != null && identityDocument.getImageUploaded()) {
            kycVerificationStatus = VerificationStepStatus.FINISHED;
            bankVerificationStatus = VerificationStepStatus.IN_PROGRESS;
        }
    }

    public HandleBankingReportApprovalResult handleBankingReportApproval(HandleBankingReportApprovalRequest request, IOpenBankingFacade openBankingFacade, IAiRecommendationFacade aiRecommendationFacade) {
        if (bankVerificationStatus != VerificationStepStatus.IN_PROGRESS) {
            return HandleBankingReportApprovalResult.bankingStepNotInProgress();
        }
        if (bankVerificationApproved != null) {
            return HandleBankingReportApprovalResult.bankingReportApprovalAlreadySent();
        }
        if (!request.getClientApproval()) {
            bankVerificationApproved = false;
            bankVerificationStatus = VerificationStepStatus.FINISHED;
            initiateAiRecommendation(aiRecommendationFacade);
        } else {
            bankVerificationApproved = true;
            var openBankingReport = openBankingFacade.initializeBankingReport(new InitializeBankingReportRequest(id));
            openBankingReportId = openBankingReport.getId();
        }
        return HandleBankingReportApprovalResult.success();
    }

    public void handleBankingReportFinished(IAiRecommendationFacade aiRecommendationFacade) {
        bankVerificationStatus = VerificationStepStatus.FINISHED;
        initiateAiRecommendation(aiRecommendationFacade);
    }

    public void handleAiSummaryFinished(IFinalDecisionFacade agreementFacade) {
        aiRecommendationStatus = VerificationStepStatus.FINISHED;

        status = VerificationStatus.AWAITING_FINAL_DECISION;
        finalSummaryStatus = VerificationStepStatus.IN_PROGRESS;
        var finalDecision = agreementFacade.initializeFinalDecision();
        finalDecisionId = finalDecision.getId();
    }

    public void handleFinalDecisionFinished(LocalDateTime currentDateTime) {
        finalSummaryStatus = VerificationStepStatus.FINISHED;
        status = VerificationStatus.VERIFIED;
        finishDate = currentDateTime;
    }

    private void initiateAiRecommendation(IAiRecommendationFacade aiRecommendationFacade) {
        aiRecommendationStatus = VerificationStepStatus.IN_PROGRESS;
        var result = aiRecommendationFacade.initializeAiRecommendation(this.id);
        aiRecommendationId = result.getId();
    }
}
