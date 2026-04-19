package pl.kaminski.powerscore.client.domain;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.util.StringUtils;
import pl.kaminski.powerscore.commons.result.Result;
import pl.kaminski.powerscore.commons.result.ResultError;

import java.util.Locale;
import java.util.Set;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NationalityVO {
    private static final Set<String> ISO_COUNTRY_CODES;

    static {
        ISO_COUNTRY_CODES = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2);
    }

    private String nationality;


    static Result<NationalityVO, NationalityVO.Error> create(String value) {
        value = value.trim();
        if (!StringUtils.hasText(value)) {
            return Result.error(NationalityVO.Error.EMPTY);
        }
        if (!ISO_COUNTRY_CODES.contains(value)) {
            return Result.error(NationalityVO.Error.INVALID);
        }
        return Result.success(new NationalityVO(value));
    }

    @RequiredArgsConstructor
    @Getter
    public enum Error implements ResultError {
        EMPTY("Nationality cannot be empty"),
        INVALID("Nationality is invalid");

        private final String message;
    }
}
