package pl.kaminski.powerscore.verification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;
import pl.kaminski.powerscore.verification.query.QVerification;

import java.util.Optional;
import java.util.UUID;

@Repository
interface VerificationQueryJpaRepository extends JpaRepository<QVerification, UUID>, JpaSpecificationExecutor<QVerification> {

    @Query("SELECT v FROM QVerification v JOIN v.identityDocument id WHERE id.type = :type AND id.number = :number")
    Optional<QVerification> findByTypeAndNumber(IdentityDocumentType type, String number);
}
