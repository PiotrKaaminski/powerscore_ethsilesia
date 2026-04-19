package pl.kaminski.powerscore.client.application.contract;

import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.client.domain.*;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.commons.result.AbstractInputValidationError;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateClientResult extends Result<CreateClientResult.CreateClientSuccess, CreateClientResult.Error> {

    private CreateClientResult(CreateClientSuccess success) {
        super(success);
    }

    private CreateClientResult(Error error) {
        super(error);
    }

    public static ValidationError.Builder errorBuilder() {
        return new ValidationError.Builder();
    }

    public static CreateClientResult error(Error error) {
        return new CreateClientResult(error);
    }

    public static CreateClientResult success(EntityId clientId) {
        return new CreateClientResult(new CreateClientSuccess(clientId.value()));
    }

    public record CreateClientSuccess(UUID clientId) {
    }

    public sealed interface Error extends ResultError {
    }

    public static non-sealed class ValidationError extends AbstractInputValidationError<ValidationError.ViolationError, ValidationError.ViolationDetails> implements Error {

        protected ValidationError(Map<ViolationError, ViolationDetails> violations) {
            super(violations);
        }

        protected ValidationError(List<Violation<ViolationError, ViolationDetails>> violations) {
            super(violations);
        }

        public static class Builder extends AbstractInputValidationError.Builder<ViolationError, ViolationDetails> {

            public void withFirstNameError(FirstNameVO.Error error) {
                var violation = switch (error) {
                    case EMPTY -> ViolationError.FIRST_NAME_EMPTY;
                    case TOO_SHORT -> ViolationError.FIRST_NAME_TOO_SHORT;
                    case TOO_LONG -> ViolationError.FIRST_NAME_TOO_LONG;
                };
                withViolation(violation);
            }

            public void withLastNameError(LastNameVO.Error error) {
                var violation = switch (error) {
                    case EMPTY -> ViolationError.LAST_NAME_EMPTY;
                    case TOO_SHORT -> ViolationError.LAST_NAME_TOO_SHORT;
                    case TOO_LONG -> ViolationError.LAST_NAME_TOO_LONG;
                };
                withViolation(violation);
            }

            public void withBirthdateError(BirthdateVO.Error error) {
                var violation = switch (error) {
                    case EMPTY -> ViolationError.BIRTHDATE_EMPTY;
                    case FUTURE_BIRTHDATE -> ViolationError.BIRTHDATE_FUTURE_BIRTHDATE;
                };
                withViolation(violation);
            }

            public void withNationalityError(NationalityVO.Error error) {
                var violation = switch (error) {
                    case EMPTY -> ViolationError.NATIONALITY_EMPTY;
                    case INVALID -> ViolationError.NATIONALITY_INVALID;
                };
                withViolation(violation);
            }

            public void withEmailError(EmailVO.Error error) {
                var violation = switch (error) {
                    case EMPTY -> ViolationError.EMAIL_EMPTY;
                    case INVALID -> ViolationError.EMAIL_INVALID;
                };
                withViolation(violation);
            }

            public void withPhoneNumberError(PhoneNumberVO.Error error) {
                var violation = switch (error) {
                    case EMPTY -> ViolationError.PHONE_NUMBER_EMPTY;
                    case INVALID_FORMAT -> ViolationError.PHONE_NUMBER_INVALID_FORMAT;
                };
                withViolation(violation);
            }

            private void withViolation(ViolationError error) {
                super.withViolation(error, new ViolationDetails(error.field, error.reason));
            }

            public ValidationError build() {
                assert hasViolations() : "cannot build error with no violations";
                return new ValidationError(super.violations);
            }
        }

        @RequiredArgsConstructor
        public enum ViolationError {
            FIRST_NAME_EMPTY(InvalidField.FIRST_NAME, InvalidReason.EMPTY),
            FIRST_NAME_TOO_SHORT(InvalidField.FIRST_NAME, InvalidReason.TOO_SHORT),
            FIRST_NAME_TOO_LONG(InvalidField.FIRST_NAME, InvalidReason.TOO_LONG),
            LAST_NAME_EMPTY(InvalidField.LAST_NAME, InvalidReason.EMPTY),
            LAST_NAME_TOO_SHORT(InvalidField.LAST_NAME, InvalidReason.TOO_SHORT),
            LAST_NAME_TOO_LONG(InvalidField.LAST_NAME, InvalidReason.TOO_LONG),
            BIRTHDATE_EMPTY(InvalidField.BIRTHDATE, InvalidReason.EMPTY),
            BIRTHDATE_FUTURE_BIRTHDATE(InvalidField.BIRTHDATE, InvalidReason.FUTURE_BIRTHDATE),
            NATIONALITY_EMPTY(InvalidField.NATIONALITY, InvalidReason.EMPTY),
            NATIONALITY_INVALID(InvalidField.NATIONALITY, InvalidReason.INVALID),
            EMAIL_EMPTY(InvalidField.EMAIL, InvalidReason.EMPTY),
            EMAIL_INVALID(InvalidField.EMAIL, InvalidReason.INVALID),
            PHONE_NUMBER_EMPTY(InvalidField.PHONE_NUMBER, InvalidReason.EMPTY),
            PHONE_NUMBER_INVALID_FORMAT(InvalidField.PHONE_NUMBER, InvalidReason.INVALID_FORMAT);

            private final InvalidField field;
            private final InvalidReason reason;
        }

        public enum InvalidField {
            FIRST_NAME,
            LAST_NAME,
            BIRTHDATE,
            NATIONALITY,
            EMAIL,
            PHONE_NUMBER
        }

        public enum InvalidReason {
            EMPTY,
            TOO_SHORT,
            TOO_LONG,
            FUTURE_BIRTHDATE,
            INVALID,
            INVALID_FORMAT
        }

        public record ViolationDetails(InvalidField field, InvalidReason reason) {
        }
    }
}
