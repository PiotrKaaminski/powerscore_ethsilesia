package pl.kaminski.powerscore.verification.query;

import pl.kaminski.powerscore.commons.PaginatedResponse;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;
import pl.kaminski.powerscore.verification.query.contract.VerificationFilter;

import java.util.Optional;
import java.util.UUID;

public interface IVerificationQueryRepository {
    Optional<QVerification> findByTypeAndNumber(IdentityDocumentType type, String number);
    Optional<QVerification> findById(UUID id);
    PaginatedResponse<QVerification> getAllVerifications(VerificationFilter filters);
    Optional<QIdentityDocumentImage> findImageById(UUID identityDocumentId);
}
