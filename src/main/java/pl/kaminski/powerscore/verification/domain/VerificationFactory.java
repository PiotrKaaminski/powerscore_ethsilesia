package pl.kaminski.powerscore.verification.domain;

import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.commons.DateTimeProvider;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.verification.application.contract.CreateVerificationRequest;
import pl.kaminski.powerscore.verification.application.contract.CreateVerificationResult;

@RequiredArgsConstructor
public class VerificationFactory {

    private final DateTimeProvider dateTimeProvider;

    public Result<Verification, CreateVerificationResult.Error> createVerification(CreateVerificationRequest request) {
        var validationErrorBuilder = CreateVerificationResult.errorBuilder();
        var verificationBuilder = Verification.builder();

        IdentityDocument.create(request.type(), request.number()).handle(verificationBuilder::identityDocument, validationErrorBuilder::withIdentityDocumentError);

        if (validationErrorBuilder.hasViolations()) {
            return Result.error(validationErrorBuilder.build());
        }

        var verification = verificationBuilder
                .id(EntityId.newId())
                .status(VerificationStatus.IN_PROGRESS)
                .startDate(dateTimeProvider.currentDateTime())
                .kycVerificationStatus(VerificationStepStatus.IN_PROGRESS)
                .bankVerificationStatus(VerificationStepStatus.WAITING_FOR_PREVIOUS)
                .aiRecommendationStatus(VerificationStepStatus.WAITING_FOR_PREVIOUS)
                .finalSummaryStatus(VerificationStepStatus.WAITING_FOR_PREVIOUS)
                .build();

        return CreateVerificationResult.success(verification);


    }
}
