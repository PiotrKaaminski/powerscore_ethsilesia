package pl.kaminski.powerscore.verification.query;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "identity_documents")
public class QIdentityDocumentImage {
    @Id
    @Column(name = "identity_document_id")
    private UUID id;
    private byte[] documentImage;

}
