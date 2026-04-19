package pl.kaminski.powerscore.client.query;

import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.client.query.contract.ClientInfo;
import pl.kaminski.powerscore.client.query.contract.GetClientResult;
import pl.kaminski.powerscore.client.query.contract.IClientQueryFacade;
import pl.kaminski.powerscore.commons.PaginatedResponse;
import pl.kaminski.powerscore.commons.PaginationFilter;

import java.util.UUID;

@RequiredArgsConstructor
public class ClientQueryFacade implements IClientQueryFacade {
    private final IClientQueryRepository repository;


    @Override
    public GetClientResult getClientById(UUID id) {
        var clientOptional = repository.getClientById(id);
        return clientOptional.map(qClient -> GetClientResult.success(qClient.toClientInfo()))
                .orElseGet(() -> GetClientResult.userNotFound(id));
    }

}
