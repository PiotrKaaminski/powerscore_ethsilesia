package pl.kaminski.powerscore.client.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.kaminski.powerscore.client.query.QClient;

import java.util.UUID;

@Repository
interface ClientQueryJpaRepository extends JpaRepository<QClient, UUID>, JpaSpecificationExecutor<QClient> {

}
