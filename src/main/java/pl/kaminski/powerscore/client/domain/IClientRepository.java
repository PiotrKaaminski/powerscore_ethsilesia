package pl.kaminski.powerscore.client.domain;

import pl.kaminski.powerscore.commons.EntityId;

import java.util.Optional;

public interface IClientRepository {
    void save(Client client);
    Optional<Client> findById(EntityId id);
}
