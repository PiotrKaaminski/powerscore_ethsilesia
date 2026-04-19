package pl.kaminski.powerscore.verification.query.contract;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;

public class GetVerificationByDocumentResult extends Result<VerificationInfo, GetVerificationByDocumentResult.Error> {

    private GetVerificationByDocumentResult(VerificationInfo verificationInfo) {super(verificationInfo);}
    private GetVerificationByDocumentResult(Error error) {super(error);}

    public static GetVerificationByDocumentResult success(VerificationInfo verificationInfo) {return new GetVerificationByDocumentResult(verificationInfo);}
    public static GetVerificationByDocumentResult verificationNotFound() {return new GetVerificationByDocumentResult(new VerificationNotFound());}

    public sealed interface Error extends ResultError { }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public record VerificationNotFound() implements Error {
        @Override
        public String getMessage() {
            return "Verification not found";
        }
    }
}
