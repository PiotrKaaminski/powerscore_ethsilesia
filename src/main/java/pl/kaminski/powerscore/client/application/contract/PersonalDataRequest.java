package pl.kaminski.powerscore.client.application.contract;

import java.time.LocalDate;

public record PersonalDataRequest(
        String firstName,
        String lastName,
        LocalDate birthdate,
        String nationality,
        String email,
        String phoneNumber
) {
}
