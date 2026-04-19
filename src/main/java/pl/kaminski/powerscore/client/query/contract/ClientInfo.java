package pl.kaminski.powerscore.client.query.contract;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ClientInfo {
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String nationality;
    private String email;
    private String phoneNumber;
}
