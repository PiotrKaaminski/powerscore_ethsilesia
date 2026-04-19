package pl.kaminski.powerscore.verification.query;

import jakarta.persistence.*;
import lombok.Data;
import pl.kaminski.powerscore.verification.domain.IdentityDocumentType;
import pl.kaminski.powerscore.verification.query.contract.IdentityDocumentInfo;

import java.util.UUID;

@Entity
@Data
@Table(name = "identity_documents")
public class QIdentityDocument {
    @Id
    @Column(name = "identity_document_id")
    private UUID id;
    @Enumerated(EnumType.STRING)
    private IdentityDocumentType type;
    private String number;
    private Boolean imageUploaded;

    public IdentityDocumentInfo toIdentityDocumentInfo() {
        return new IdentityDocumentInfo(id, type, number, imageUploaded);
    }
}
