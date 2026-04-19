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
public class PhoneNumberVO {

    private String phoneNumber;

    static Result<PhoneNumberVO, Error> create(String value) {
        value = value.trim();
        if (!StringUtils.hasText(value)) {
            return Result.error(Error.EMPTY);
        }
        if (!value.matches("\\+[0-9]+")) {
            return Result.error(Error.INVALID_FORMAT);
        }
        return Result.success(new PhoneNumberVO(value));
    }

    @RequiredArgsConstructor
    @Getter
    public enum Error implements ResultError {
        EMPTY("PhoneNumber cannot be empty"),
        INVALID_FORMAT("PhoneNumber format is invalid");

        private final String message;
    }
}
