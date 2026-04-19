package pl.kaminski.powerscore.verification.domain;

import jakarta.persistence.*;
import lombok.*;
import pl.kaminski.powerscore.commons.EntityId;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;
import pl.kaminski.powerscore.verification.application.contract.UploadDocumentImageResult;

import java.util.Set;

@Entity
@Table(name = "identity_documents")
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class IdentityDocument {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/png", "image/jpeg", "image/jpg");

    @Id
    @AttributeOverride(name = "value", column = @Column(name = "identity_document_id"))
    private EntityId id;
    @Enumerated(EnumType.STRING)
    private IdentityDocumentType type;
    private String number;
    private byte[] documentImage;
    private Boolean imageUploaded;

    public UploadDocumentImageResult uploadDocumentImage(byte[] documentImage, String contentType) {
        if (imageUploaded) {
            return UploadDocumentImageResult.imageAlreadyUploaded();
        }
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            return UploadDocumentImageResult.fileIsNotImage();
        }
        if (documentImage == null || documentImage.length == 0) {
            return UploadDocumentImageResult.imageEmpty();
        }
        this.documentImage = documentImage;
        this.imageUploaded = true;
        return UploadDocumentImageResult.success();
    }

    public static Result<IdentityDocument, Error> create(IdentityDocumentType type, String number) {
        if (type == null) {
            return Result.error(Error.DOCUMENT_TYPE_EMPTY);
        }
        if (number == null || number.isBlank()) {
            return Result.error(Error.DOCUMENT_NUMBER_EMPTY);
        } else if (IdentityDocumentType.PESEL.equals(type) && !isPeselValid(number)) {
            return Result.error(Error.PESEL_INVALID);
        }
        return Result.success(new IdentityDocument(EntityId.newId(), type, number, null, false));
    }

    private static boolean isPeselValid(String pesel) {
        if (pesel == null || pesel.length() != 11 || !pesel.matches("\\d{11}")) {
            return false;
        }
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += Character.getNumericValue(pesel.charAt(i)) * weights[i];
        }
        int checksum = (10 - (sum % 10)) % 10;
        return checksum == Character.getNumericValue(pesel.charAt(10));
    }


    @RequiredArgsConstructor
    @Getter
    public enum Error implements ResultError {
        DOCUMENT_NUMBER_EMPTY("Document number cannot be empty"),
        PESEL_INVALID("Pesel is invalid"),
        DOCUMENT_TYPE_EMPTY("Document type cannot be empty");

        private final String message;
    }
}
