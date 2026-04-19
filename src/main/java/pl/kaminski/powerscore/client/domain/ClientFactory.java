package pl.kaminski.powerscore.client.domain;

import pl.kaminski.powerscore.client.application.contract.PersonalDataRequest;
import pl.kaminski.powerscore.client.application.contract.CreateClientResult;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.commons.result.Result;

public class ClientFactory {

    public Result<Client, CreateClientResult.Error> createClient(PersonalDataRequest request, EntityId identityDocumentId) {
        var errorBuilder = CreateClientResult.errorBuilder();
        var clientBuilder = Client.builder();

        FirstNameVO.create(request.firstName()).handle(clientBuilder::firstName, errorBuilder::withFirstNameError);
        LastNameVO.create(request.lastName()).handle(clientBuilder::lastName, errorBuilder::withLastNameError);
        BirthdateVO.create(request.birthdate()).handle(clientBuilder::birthdate, errorBuilder::withBirthdateError);
        NationalityVO.create(request.nationality()).handle(clientBuilder::nationality, errorBuilder::withNationalityError);
        EmailVO.create(request.email()).handle(clientBuilder::email, errorBuilder::withEmailError);
        PhoneNumberVO.create(request.phoneNumber()).handle(clientBuilder::phoneNumber, errorBuilder::withPhoneNumberError);

        if (errorBuilder.hasViolations()) {
            return Result.error(errorBuilder.build());
        }

        var client = clientBuilder
                .id(EntityId.newId())
                .identityDocumentId(identityDocumentId)
                .build();

        return Result.success(client);
    }
}
