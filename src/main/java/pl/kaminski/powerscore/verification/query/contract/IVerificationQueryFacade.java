package pl.kaminski.powerscore.verification.query.contract;

import pl.kaminski.powerscore.commons.PaginatedResponse;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;

import java.util.UUID;

public interface IVerificationQueryFacade {
    GetVerificationByDocumentResult getVerificationByDocument(IdentityDocumentType type, String documentNumber);
    GetVerificationResult getVerification(UUID id);
    PaginatedResponse<VerificationInfo> getAllVerifications(VerificationFilter filters);
    GetDocumentImageResult getDocumentImage(UUID id);
}
