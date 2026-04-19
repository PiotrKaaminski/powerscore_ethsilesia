package pl.kaminski.powerscore.verification.query.contract;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;

public class GetDocumentImageResult extends Result<byte[], GetDocumentImageResult.Error> {

    private GetDocumentImageResult(byte[] documentImage) {
        super(documentImage);
    }

    private GetDocumentImageResult(Error error) {
        super(error);
    }

    public static GetDocumentImageResult success(byte[] documentImage) {
        return new GetDocumentImageResult(documentImage);
    }

    public static GetDocumentImageResult verificationNotFound() {
        return new GetDocumentImageResult(new VerificationNotFound());
    }

    public static GetDocumentImageResult imageNotUploaded() {
        return new GetDocumentImageResult(new ImageNotUploaded());
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public record ImageNotUploaded() implements Error {
        @Override
        public String getMessage() {
            return "Image not uploaded";
        }
    }
}
