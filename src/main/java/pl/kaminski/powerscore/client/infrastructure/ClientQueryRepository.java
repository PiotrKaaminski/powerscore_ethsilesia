package pl.kaminski.powerscore.client.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import pl.kaminski.powerscore.client.query.QClient;
import pl.kaminski.powerscore.client.query.QClient_;
import pl.kaminski.powerscore.client.query.IClientQueryRepository;
import pl.kaminski.powerscore.commons.PaginatedResponse;
import pl.kaminski.powerscore.commons.PaginationFilter;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
class ClientQueryRepository implements IClientQueryRepository {

    private final ClientQueryJpaRepository jpaRepository;


    @Override
    public Optional<QClient> getClientById(UUID id) {
        return jpaRepository.findById(id);
    }

}
