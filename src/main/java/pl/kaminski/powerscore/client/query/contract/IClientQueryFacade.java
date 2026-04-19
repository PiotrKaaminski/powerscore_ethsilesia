package pl.kaminski.powerscore.client.query.contract;

import pl.kaminski.powerscore.commons.PaginatedResponse;
import pl.kaminski.powerscore.commons.PaginationFilter;

import java.util.UUID;

public interface IClientQueryFacade {
    GetClientResult getClientById(UUID id);
}
