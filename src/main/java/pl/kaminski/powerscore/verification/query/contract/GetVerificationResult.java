package pl.kaminski.powerscore.verification.query.contract;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;

public class GetVerificationResult extends Result<VerificationInfo, GetVerificationResult.Error> {

    private GetVerificationResult(VerificationInfo verificationInfo) {
        super(verificationInfo);
    }

    private GetVerificationResult(Error error) {
        super(error);
    }

    public static GetVerificationResult success(VerificationInfo verificationInfo) {
        return new GetVerificationResult(verificationInfo);
    }

    public static GetVerificationResult verificationNotFound() {
        return new GetVerificationResult(new VerificationNotFound());
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
}
