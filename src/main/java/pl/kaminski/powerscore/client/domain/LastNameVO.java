package pl.kaminski.powerscore.client.domain;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.util.StringUtils;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastNameVO {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;

    private String lastName;

    static Result<LastNameVO, Error> create(String value) {
        value = value.trim();
        if (!StringUtils.hasText(value)) {
            return Result.error(Error.EMPTY);
        }
        if (value.length() < MIN_LENGTH) {
            return Result.error(Error.TOO_SHORT);
        }
        if (value.length() > MAX_LENGTH) {
            return Result.error(Error.TOO_LONG);
        }
        return Result.success(new LastNameVO(value));
    }

    @RequiredArgsConstructor
    @Getter
    public enum Error implements ResultError {
        EMPTY("LastName cannot be empty"),
        TOO_SHORT("LastName is too short, min length: " + MIN_LENGTH),
        TOO_LONG("LastName is too long, max length: " + MAX_LENGTH);

        private final String message;
    }
}
