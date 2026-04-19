package pl.kaminski.powerscore.client.application.contract;

import pl.kaminski.powerscore.commons.EntityId;

public interface IClientFacade {
    CreateClientResult createClient(PersonalDataRequest request, EntityId identityDocumentId);
}
