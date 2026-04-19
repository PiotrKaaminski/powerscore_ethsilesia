package pl.kaminski.powerscore.client.domain;

import jakarta.persistence.*;
import lombok.*;
import pl.kaminski.powerscore.commons.EntityId;

@Entity
@Table(name = "clients")
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Client {
    @Id
    @AttributeOverride(name = "value", column = @Column(name = "client_id"))
    private EntityId id;
    private FirstNameVO firstName;
    private LastNameVO lastName;
    private BirthdateVO birthdate;
    private NationalityVO nationality;
    private EmailVO email;
    private PhoneNumberVO phoneNumber;
    @AttributeOverride(name = "value", column = @Column(name = "identity_document_id"))
    private EntityId identityDocumentId;
}
