package pl.kaminski.powerscore.verification.application.contract;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kaminski.powerscore.commons.result.EmptyResult;
import pl.kaminski.powerscore.commons.result.ResultError;

public class UploadDocumentImageResult extends EmptyResult<UploadDocumentImageResult.Error> {
    private UploadDocumentImageResult() {
        super();
    }

    private UploadDocumentImageResult(Error error) {
        super(error);
    }

    public static UploadDocumentImageResult success() {
        return new UploadDocumentImageResult();
    }

    public static UploadDocumentImageResult error(Error error) {
        return new UploadDocumentImageResult(error);
    }

    public static UploadDocumentImageResult verificationNotFound() {
        return new UploadDocumentImageResult(new VerificationNotFound());
    }

    public static UploadDocumentImageResult imageAlreadyUploaded() {
        return new UploadDocumentImageResult(new ImageAlreadyUploaded());
    }

    public static UploadDocumentImageResult imageEmpty() {
        return new UploadDocumentImageResult(new ImageEmpty());
    }

    public static UploadDocumentImageResult fileIsNotImage() {
        return new UploadDocumentImageResult(new FileIsNotImage());
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
    public record ImageAlreadyUploaded() implements Error {
        @Override
        public String getMessage() {
            return "Image already uploaded for this verification";
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public record ImageEmpty() implements Error {
        @Override
        public String getMessage() {
            return "Uploaded image is empty";
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public record FileIsNotImage() implements Error {
        @Override
        public String getMessage() {
            return "Uploaded file is not an image (png, jpg, jpeg)";
        }
    }
}
