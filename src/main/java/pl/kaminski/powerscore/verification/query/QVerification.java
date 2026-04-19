package pl.kaminski.powerscore.verification.query;

import jakarta.persistence.*;
import lombok.Getter;
import pl.kaminski.powerscore.final_decision.query.QFinalDecision;
import pl.kaminski.powerscore.ai_recommendation.query.QAiRecommendation;
import pl.kaminski.powerscore.client.query.QClient;
import pl.kaminski.powerscore.open_banking.query.QOpenBankingReport;
import pl.kaminski.powerscore.verification.domain.VerificationStatus;
import pl.kaminski.powerscore.verification.domain.VerificationStepStatus;
import pl.kaminski.powerscore.verification.query.contract.VerificationInfo;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "verifications")
public class QVerification {
    @Id
    @Column(name = "verification_id")
    private UUID id;
    @Enumerated(EnumType.STRING)
    private VerificationStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private QClient client;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_document_id")
    private QIdentityDocument identityDocument;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "open_banking_report_id")
    private QOpenBankingReport openBankingReport;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_recommendation_id")
    private QAiRecommendation aiRecommendation;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "final_decision_id")
    private QFinalDecision finalDecision;

    public VerificationInfo toVerificationInfo() {
        return new VerificationInfo(
                id,
                status,
                client != null ? client.toClientInfo() : null,
                identityDocument.toIdentityDocumentInfo(),
                startDate,
                finishDate,
                kycVerificationStatus,
                bankVerificationStatus,
                aiRecommendationStatus,
                finalSummaryStatus,
                bankVerificationApproved,
                openBankingReport != null ? openBankingReport.asDto() : null,
                aiRecommendation != null ? aiRecommendation.asDto() : null,
                finalDecision != null ? finalDecision.asDto() : null
                );
    }

}
