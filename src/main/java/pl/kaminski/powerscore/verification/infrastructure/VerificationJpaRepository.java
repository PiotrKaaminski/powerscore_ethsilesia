package pl.kaminski.powerscore.verification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;
import pl.kaminski.powerscore.verification.domain.Verification;

import java.util.Optional;


@Repository
interface VerificationJpaRepository extends JpaRepository<Verification, EntityId> {

    @Query("SELECT v.id FROM Verification v JOIN v.identityDocument idoc WHERE idoc.type = :type AND idoc.number = :number")
    Optional<EntityId> findVerificationIdByIdentityDocumentTypeAndNumber(IdentityDocumentType type, String number);

    Optional<Verification> findByOpenBankingReportId(EntityId id);
    Optional<Verification> findByAiRecommendationId(EntityId id);

    Optional<Verification> findByFinalDecisionId(EntityId id);

}
