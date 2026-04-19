package pl.kaminski.powerscore.verification.query;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.kaminski.powerscore.commons.PaginatedResponse;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;
import pl.kaminski.powerscore.verification.query.contract.*;

import java.util.UUID;

@RequiredArgsConstructor
public class VerificationQueryFacade implements IVerificationQueryFacade {

    private final IVerificationQueryRepository verificationQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public GetVerificationByDocumentResult getVerificationByDocument(IdentityDocumentType type, String documentNumber) {
        var verificationOptional = verificationQueryRepository.findByTypeAndNumber(type, documentNumber);
        if (verificationOptional.isEmpty()) {
            return GetVerificationByDocumentResult.verificationNotFound();
        }
        var verification = verificationOptional.get();
        return GetVerificationByDocumentResult.success(verification.toVerificationInfo());
    }

    @Override
    @Transactional(readOnly = true)
    public GetVerificationResult getVerification(UUID id) {
        return verificationQueryRepository.findById(id)
                .map(QVerification::toVerificationInfo)
                .map(GetVerificationResult::success)
                .orElseGet(GetVerificationResult::verificationNotFound);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<VerificationInfo> getAllVerifications(VerificationFilter filters) {
        if (filters == null) {
            filters = new VerificationFilter();
        }
        var paginatedResponse = verificationQueryRepository.getAllVerifications(filters);
        return paginatedResponse.mapRows(QVerification::toVerificationInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public GetDocumentImageResult getDocumentImage(UUID id) {
        var verificationOptional = verificationQueryRepository.findById(id);
        if (verificationOptional.isEmpty()) {
            return GetDocumentImageResult.verificationNotFound();
        }

        var verification = verificationOptional.get();
        var identityDocument = verification.getIdentityDocument();

        if (identityDocument.getImageUploaded() == null || !identityDocument.getImageUploaded()) {
            return GetDocumentImageResult.imageNotUploaded();
        }

        return verificationQueryRepository.findImageById(identityDocument.getId())
                .map(QIdentityDocumentImage::getDocumentImage)
                .map(GetDocumentImageResult::success)
                .orElseGet(GetDocumentImageResult::verificationNotFound);
    }
}
