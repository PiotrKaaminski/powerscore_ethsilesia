package pl.kaminski.powerscore.commons.result;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractInputValidationError<V extends Enum<V>, D> implements ResultError {

    @Getter
    private final List<Violation<V, D>> violations;

    protected AbstractInputValidationError(Map<V, D> violations) {
        assert violations != null && !violations.isEmpty() : "cannot create input validation reason with no violations";
        this.violations = violations.entrySet().stream()
                .map(e -> new Violation<>(e.getKey(), e.getValue()))
                .toList();
    }

    protected AbstractInputValidationError(List<Violation<V, D>> violations) {
        this.violations = violations;
    }

    public Set<V> violationKeys() {
        return violations.stream().map(Violation::violation).collect(Collectors.toSet());
    }

    @Override
    public String getMessage() {
        return "input data is invalid";
    }

    @Override
    public String additionalDataString() {
        return "violations=[\n" +
                violationsAsString() +
                "\n\t]";
    }

    private String violationsAsString() {
        return violations.stream()
                .map(v -> String.format("\t\t%s=%s", v.violation(), v.details.toString()))
                .collect(Collectors.joining(", \n"));
    }

    public static class Builder<V extends Enum<V>, I> {

        protected final Map<V, I> violations = new HashMap<>();

        protected void withViolation(V violation, I details) {
            assert violation != null;
            assert details != null;
            violations.put(violation, details);
        }

        public boolean hasViolations() {
            return !violations.isEmpty();
        }
    }

    protected record Violation<V extends Enum<V>, D>(
            V violation,
            D details
    ) {}

}
