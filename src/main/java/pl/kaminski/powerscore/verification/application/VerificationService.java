package pl.kaminski.powerscore.verification.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.ai_recommendation.application.contract.IAiRecommendationFacade;
import pl.kaminski.powerscore.open_banking.application.contract.IOpenBankingFacade;
import pl.kaminski.powerscore.verification.application.contract.*;
import pl.kaminski.powerscore.verification.domain.IVerificationRepository;
import pl.kaminski.powerscore.verification.domain.VerificationFactory;

import pl.kaminski.powerscore.client.application.contract.IClientFacade;
import pl.kaminski.powerscore.client.application.contract.PersonalDataRequest;
import pl.kaminski.powerscore.commons.EntityId;

import java.util.UUID;

@RequiredArgsConstructor
public class VerificationService implements IVerificationService {
    private final IVerificationRepository verificationRepository;
    private final VerificationFactory verificationFactory;
    private final IClientFacade clientFacade;
    private final IOpenBankingFacade openBankingFacade;
    private final IAiRecommendationFacade aiRecommendationFacade;

    @Transactional
    public CreateVerificationResult createVerification(CreateVerificationRequest request) {

        var existingVerificationId = verificationRepository.findVerificationIdByIdentityDocumentTypeAndNumber(request.type(), request.number());
        if (existingVerificationId.isPresent()) {
            return CreateVerificationResult.success(existingVerificationId.get(), VerificationType.EXISTING);

        }
        var verificationCreate = verificationFactory.createVerification(request);
        if (verificationCreate.isError()) {
            return CreateVerificationResult.error(verificationCreate.getError());
        }

        var verification = verificationCreate.getSuccess();
        verificationRepository.save(verification);
        return CreateVerificationResult.success(verification.getId(), VerificationType.NEW);
    }

    @Override
    @Transactional
    public AddPersonalDataResult addPersonalData(UUID verificationId, PersonalDataRequest request) {
        var verification = verificationRepository.findById(EntityId.from(verificationId))
                .orElse(null);

        if (verification == null) {
            return AddPersonalDataResult.verificationNotFound();
        }

        if (verification.getClientId() != null) {
            return AddPersonalDataResult.clientAlreadyAssigned();
        }

        var clientCreateResult = clientFacade.createClient(request, verification.getIdentityDocument().getId());
        if (clientCreateResult.isError()) {
            return AddPersonalDataResult.from(clientCreateResult.getError());
        }

        var clientId = clientCreateResult.getSuccess().clientId();
        verification.setClientId(EntityId.from(clientId));
        verification.checkKycVerificationFinished();
        verificationRepository.save(verification);
        return AddPersonalDataResult.success(verification.getClientId());
    }

    @Override
    @Transactional
    public UploadDocumentImageResult uploadDocumentImage(UUID verificationId, byte[] documentImage, String contentType) {
        var result = verificationRepository.findById(EntityId.from(verificationId));
        if (result.isEmpty()) {
            return UploadDocumentImageResult.verificationNotFound();
        }
        var verification = result.get();
        var uploadDocumentImageResult = verification.uploadDocumentImage(documentImage, contentType);

        if (uploadDocumentImageResult.isError()) {
            return uploadDocumentImageResult;
        }
        verification.checkKycVerificationFinished();
        return UploadDocumentImageResult.success();
    }

    @Override
    @Transactional
    public HandleBankingReportApprovalResult provideBankingReportApproval(UUID verificationId, HandleBankingReportApprovalRequest request) {
        if (request == null || request.getClientApproval() == null) {
            return HandleBankingReportApprovalResult.bankingReportClientApprovalEmpty();
        }
        var verificationOptional = verificationRepository.findById(EntityId.from(verificationId));
        if (verificationOptional.isEmpty()) {
            return HandleBankingReportApprovalResult.verificationNotFound();
        }
        var verification = verificationOptional.get();
        return verification.handleBankingReportApproval(request, openBankingFacade, aiRecommendationFacade);
    }


}
