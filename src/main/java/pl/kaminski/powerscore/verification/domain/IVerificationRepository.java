package pl.kaminski.powerscore.verification.domain;

import pl.kaminski.powerscore.commons.EntityId;

import java.util.Optional;

public interface IVerificationRepository {
    Optional<EntityId> findVerificationIdByIdentityDocumentTypeAndNumber(IdentityDocumentType type, String number);
    Optional<Verification> findById(EntityId id);
    Optional<Verification> findByOpenBankingReportId(EntityId id);
    Optional<Verification> findByFinalDecisionId(EntityId id);
    Optional<Verification> findByAiRecommendationId(EntityId id);
    void save(Verification verification);
}
