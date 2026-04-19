package pl.kaminski.powerscore.verification.query.contract;

import lombok.Data;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;

import java.util.List;

@Data
public class IdentityDocumentFilters {
    private List<IdentityDocumentType> identityDocumentTypes;
    private String number;
}
