package pl.kaminski.powerscore.web.rest;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.kaminski.powerscore.client.query.contract.ClientInfo;
import pl.kaminski.powerscore.client.query.contract.GetClientResult;
import pl.kaminski.powerscore.client.query.contract.IClientQueryFacade;
import pl.kaminski.powerscore.commons.PaginatedResponse;
import pl.kaminski.powerscore.commons.PaginationFilter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final IClientQueryFacade clientQueryFacade;

    @GetMapping("/client/{id}")
    public GetClientResult getClient(@PathVariable UUID id) {
        return clientQueryFacade.getClientById(id);
    }

}
