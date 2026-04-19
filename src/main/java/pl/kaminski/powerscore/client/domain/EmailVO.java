package pl.kaminski.powerscore.client.domain;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVO {

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private String email;

    static Result<EmailVO, Error> create(String value) {
        value = value.trim();
        if (!StringUtils.hasText(value)) {
            return Result.error(Error.EMPTY);
        }

        if (!EMAIL_VALIDATOR.isValid(value)) {
            return Result.error(Error.INVALID);
        }
        return Result.success(new EmailVO(value));
    }

    @RequiredArgsConstructor
    @Getter
    public enum Error implements ResultError {
        EMPTY("Email cannot be empty"),
        INVALID("Email is invalid");

        private final String message;
    }
}
