package pl.kaminski.powerscore.client.application;

import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.client.application.contract.IClientFacade;
import pl.kaminski.powerscore.client.application.contract.PersonalDataRequest;
import pl.kaminski.powerscore.client.application.contract.CreateClientResult;
import pl.kaminski.powerscore.client.domain.ClientFactory;
import pl.kaminski.powerscore.client.domain.IClientRepository;
import pl.kaminski.powerscore.commons.EntityId;

@RequiredArgsConstructor
public class ClientFacade implements IClientFacade {

    private final IClientRepository clientRepository;
    private final ClientFactory clientFactory;

    @Override
    public CreateClientResult createClient(PersonalDataRequest request, EntityId identityDocumentId) {
        var createResult = clientFactory.createClient(request, identityDocumentId);
        if (createResult.isError()) {
            return CreateClientResult.error(createResult.getError());
        }

        var client = createResult.getSuccess();
        clientRepository.save(client);

        return CreateClientResult.success(client.getId());
    }
}
