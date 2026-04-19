package pl.kaminski.powerscore.commons;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeProvider {

    public LocalDateTime currentDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }
}
