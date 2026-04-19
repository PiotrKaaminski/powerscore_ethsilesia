package pl.kaminski.powerscore.commons.result;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ResultError {

    @JsonProperty(index = 11)
    String getMessage();

    @JsonProperty(index = 10)
    default String getErrorCode() {
        // jeśli instanceof Enum to zwrócić wartośc enuma
        if (this instanceof Enum<?> e) {
            return e.name();
        }
        // w innym wypadku zwrócić nazwę klasy zmieniająć postać z NazwaKlasy do NAZWA_KLASY
        return ResultUtils.classNameToConstantCase(this.getClass());
    }

    default String fullMessage() {
        return fullMessagePretty()
                .replace("\n", "")
                .replace("\t", "");
    }

    default String fullMessagePretty() {
        return "(\n" +
                "message=" + getMessage() + ", \n" +
                "errorCode=" + getErrorCode() + ", \n" +
                "additionalData(\n\t" + additionalDataString() + "\n" +
                ")";
    }

    default String additionalDataString() {
        return "";
    }

}
