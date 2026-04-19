package pl.kaminski.powerscore.client.query;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pl.kaminski.powerscore.client.query.contract.ClientInfo;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "clients")
public class QClient {
    @Id
    @Column(name = "client_id")
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String nationality;
    private String email;
    private String phoneNumber;

    public ClientInfo toClientInfo() {
        return new ClientInfo(id, firstName, lastName, birthdate, nationality, email, phoneNumber);
    }
}
