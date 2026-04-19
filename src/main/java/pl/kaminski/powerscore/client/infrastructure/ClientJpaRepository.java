package pl.kaminski.powerscore.client.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kaminski.powerscore.client.domain.Client;
import pl.kaminski.powerscore.commons.EntityId;

@Repository
interface ClientJpaRepository extends JpaRepository<Client, EntityId> {
}
