package pl.kaminski.powerscore.verification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kaminski.powerscore.verification.query.QIdentityDocumentImage;

import java.util.UUID;

@Repository
interface QIdentityDocumentImageJpaRepository extends JpaRepository<QIdentityDocumentImage, UUID> {
}
