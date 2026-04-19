package pl.kaminski.powerscore.verification.application.contract;

import pl.kaminski.powerscore.client.application.contract.PersonalDataRequest;

import java.util.UUID;

public interface IVerificationService {
    CreateVerificationResult createVerification(CreateVerificationRequest request);
    AddPersonalDataResult addPersonalData(UUID verificationId, PersonalDataRequest request);
    UploadDocumentImageResult uploadDocumentImage(UUID verificationId, byte[] image, String contentType);
    HandleBankingReportApprovalResult provideBankingReportApproval(UUID verificationId, HandleBankingReportApprovalRequest request);
}
