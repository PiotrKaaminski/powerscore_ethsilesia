package pl.kaminski.powerscore.verification.application.contract;

import lombok.RequiredArgsConstructor;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.commons.result.AbstractInputValidationError;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;
import pl.kaminski.powerscore.verification.domain.IdentityDocument;

import java.util.Map;
import java.util.UUID;

public class CreateVerificationResult extends Result<CreateVerificationResult.CreateVerificationSuccess, CreateVerificationResult.Error> {

    private CreateVerificationResult(CreateVerificationSuccess success) {super(success);}
    private CreateVerificationResult(Error error) {super(error);}

    public static ValidationError.Builder errorBuilder() {return new ValidationError.Builder();}
    public static CreateVerificationResult error(Error error) {return new CreateVerificationResult(error);}
    public static CreateVerificationResult success(EntityId verificationId, VerificationType type) {
        return new CreateVerificationResult(new CreateVerificationSuccess(verificationId.value(), type));
    }
    public record CreateVerificationSuccess(UUID verificationId, VerificationType type) { }
    public sealed interface Error extends ResultError { }

    public static final class ValidationError extends AbstractInputValidationError<ValidationError.ViolationError, ValidationError.ViolationDetails> implements Error {

        ValidationError(Map<ViolationError, ViolationDetails> violations) {super(violations);}

        public static class Builder extends AbstractInputValidationError.Builder<ViolationError, ViolationDetails> {


            public void withIdentityDocumentError(IdentityDocument.Error error) {
                var violation = switch (error) {
                    case DOCUMENT_NUMBER_EMPTY -> ViolationError.DOCUMENT_NUMBER_EMPTY;
                    case PESEL_INVALID -> ViolationError.PESEL_INVALID;
                    case DOCUMENT_TYPE_EMPTY -> ViolationError.DOCUMENT_TYPE_EMPTY;
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
            DOCUMENT_NUMBER_EMPTY(InvalidField.DOCUMENT_NUMBER, InvalidReason.EMPTY),
            DOCUMENT_TYPE_EMPTY(InvalidField.DOCUMENT_TYPE, InvalidReason.EMPTY),
            PESEL_INVALID(InvalidField.DOCUMENT_NUMBER, InvalidReason.INVALID_PESEL_FORMAT);


            private final InvalidField field;
            private final InvalidReason reason;
        }

        public enum InvalidField {
            DOCUMENT_NUMBER,
            DOCUMENT_TYPE
        }
        public enum InvalidReason {
            EMPTY,
            INVALID_PESEL_FORMAT
        }

        public record ViolationDetails(InvalidField field, InvalidReason reason) { }
    }

}
