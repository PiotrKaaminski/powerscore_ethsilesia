package pl.kaminski.powerscore.verification.application.contract;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kaminski.powerscore.client.application.contract.CreateClientResult;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;

import java.util.List;
import java.util.UUID;

public class AddPersonalDataResult extends Result<AddPersonalDataResult.AddPersonalDataSuccess, AddPersonalDataResult.Error> {
    private AddPersonalDataResult(AddPersonalDataSuccess success) {
        super(success);
    }
    private AddPersonalDataResult(Error error) {
        super(error);
    }


    public static AddPersonalDataResult error(Error error) {
        return new AddPersonalDataResult(error);
    }
    public static AddPersonalDataResult success(EntityId clientId) {
        return new AddPersonalDataResult(new AddPersonalDataSuccess(clientId.value()));
    }
    public static AddPersonalDataResult verificationNotFound() {return new AddPersonalDataResult(new VerificationNotFound());}
    public static AddPersonalDataResult clientAlreadyAssigned() {return new AddPersonalDataResult(new ClientAlreadyAssigned());}
    public static AddPersonalDataResult from(CreateClientResult.Error error) {
        var resultError = switch (error) {
            case CreateClientResult.ValidationError v -> new ValidationError(v.getViolations());
        };
        return AddPersonalDataResult.error(resultError);
    }

    public record AddPersonalDataSuccess(UUID clientId) {
    }

    public sealed interface Error extends ResultError {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public record VerificationNotFound() implements Error {
        @Override
        public String getMessage() {
            return "Verification not found";
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public record ClientAlreadyAssigned() implements Error {
        @Override
        public String getMessage() {
            return "Client already assigned to this verification";
        }
    }

    public static final class ValidationError extends CreateClientResult.ValidationError implements Error {
        private ValidationError(List<Violation<ViolationError, ViolationDetails>> violations) {
            super(violations);
        }
    }
}
