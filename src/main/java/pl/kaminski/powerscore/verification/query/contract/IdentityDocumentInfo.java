package pl.kaminski.powerscore.verification.query.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;

import java.util.UUID;

@AllArgsConstructor
@Data
public class IdentityDocumentInfo {
    private UUID id;
    private IdentityDocumentType type;
    private String number;
    private Boolean imageUploaded;
}
