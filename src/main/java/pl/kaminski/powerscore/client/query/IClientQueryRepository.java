package pl.kaminski.powerscore.client.query;

import pl.kaminski.powerscore.commons.PaginatedResponse;
import pl.kaminski.powerscore.commons.PaginationFilter;

import java.util.Optional;
import java.util.UUID;

public interface IClientQueryRepository {
    Optional<QClient> getClientById(UUID id);
}
