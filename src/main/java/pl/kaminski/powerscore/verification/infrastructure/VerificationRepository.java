package pl.kaminski.powerscore.verification.infrastructure;

import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.verification.domain.IVerificationRepository;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;
import pl.kaminski.powerscore.verification.domain.Verification;

import java.util.Optional;

@RequiredArgsConstructor
class VerificationRepository implements IVerificationRepository {
    private final VerificationJpaRepository jpaRepository;



    @Override
    public Optional<EntityId> findVerificationIdByIdentityDocumentTypeAndNumber(IdentityDocumentType type, String number) {
        return jpaRepository.findVerificationIdByIdentityDocumentTypeAndNumber(type, number);
    }

    @Override
    public Optional<Verification> findById(EntityId id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Verification> findByOpenBankingReportId(EntityId id) {
        return jpaRepository.findByOpenBankingReportId(id);
    }

    @Override
    public Optional<Verification> findByAiRecommendationId(EntityId id) {
        return jpaRepository.findByAiRecommendationId(id);
    }

    @Override
    public Optional<Verification> findByFinalDecisionId(EntityId id) {
        return jpaRepository.findByFinalDecisionId(id);
    }

    @Override
    public void save(Verification verification) {
        jpaRepository.save(verification);
    }
}
