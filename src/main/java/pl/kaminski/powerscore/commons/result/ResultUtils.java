package pl.kaminski.powerscore.commons.result;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ResultUtils {

    private static final String CLASS_NAME_PARTS_REGEX = "([A-Z])([a-z0-9])*";

    public static String classNameToConstantCase(Class<?> clazz) {
        Matcher m = Pattern.compile(CLASS_NAME_PARTS_REGEX).matcher(clazz.getSimpleName());
        StringBuilder result = new StringBuilder();
        var found = m.find();
        while (found) {
            result.append(m.group().toUpperCase());
            found = m.find();
            if (found) {
                result.append("_");
            }
        }
        return result.toString();
    }

    public static Set<String> getErrorCodes(Class<?> clazz) {
        if (!ResultError.class.isAssignableFrom(clazz)) {
            return Collections.emptySet();
        }
        if (clazz.isEnum()) {
            return Arrays.stream(clazz.getEnumConstants())
                    .map(e -> ((Enum<?>) e).name())
                    .collect(Collectors.toSet());
        } else {
            return Set.of(classNameToConstantCase(clazz));
        }
    }
}
