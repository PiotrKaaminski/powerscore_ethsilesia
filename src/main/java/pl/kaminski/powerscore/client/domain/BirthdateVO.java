package pl.kaminski.powerscore.client.domain;

import jakarta.persistence.Embeddable;
import lombok.*;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;

import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BirthdateVO {


    private LocalDate birthdate;

    static Result<BirthdateVO, Error> create(LocalDate value) {
        if (value == null) {
            return Result.error(Error.EMPTY);
        }
        if (value.isAfter(LocalDate.now())) {
            return Result.error(Error.FUTURE_BIRTHDATE);
        }
        return Result.success(new BirthdateVO(value));
    }

    @RequiredArgsConstructor
    @Getter
    public enum Error implements ResultError {
        EMPTY("Birthdate cannot be empty"),
        FUTURE_BIRTHDATE("Birthdate cannot be in the future");

        private final String message;
    }
}
