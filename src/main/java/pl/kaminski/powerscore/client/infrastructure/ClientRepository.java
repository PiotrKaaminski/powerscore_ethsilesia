package pl.kaminski.powerscore.client.infrastructure;

import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.client.domain.Client;
import pl.kaminski.powerscore.client.domain.IClientRepository;
import pl.kaminski.powerscore.commons.EntityId;

import java.util.Optional;

@RequiredArgsConstructor
class ClientRepository implements IClientRepository {

    private final ClientJpaRepository jpaRepository;

    @Override
    public void save(Client client) {
        jpaRepository.save(client);
    }

    @Override
    public Optional<Client> findById(EntityId id) {
        return jpaRepository.findById(id);
    }
}
