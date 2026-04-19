package pl.kaminski.powerscore.verification.query.contract;

import lombok.Data;
import pl.kaminski.powerscore.commons.DateRange;

@Data
public class ClientFilters {
    private String firstName;
    private String lastName;
    private DateRange birthdate;
    private String nationality;
    private String email;
    private String phoneNumber;
}
