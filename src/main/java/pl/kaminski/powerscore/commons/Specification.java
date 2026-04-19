package pl.kaminski.powerscore.commons;

import java.util.function.Consumer;

/**
 * Functional interface with helper methods for checking if object of type T satisfies some condition
 * @param <T>
 */
@FunctionalInterface
public interface Specification<T> {

    boolean isSatisfiedBy(T t);

    default boolean isNotSatisfiedBy(T t) {
        return !isSatisfiedBy(t);
    }

    default void isSatisfiedBy(T t, Runnable isSatisfied, Runnable isNotSatisfied) {
        if (isSatisfiedBy(t)) {
            isSatisfied.run();
        } else {
            isNotSatisfied.run();
        }
    }

    default void isSatisfiedBy(T t, Consumer<T> isSatisfied, Consumer<T> isNotSatisfied) {
        if (isSatisfiedBy(t)) {
            isSatisfied.accept(t);
        } else {
            isNotSatisfied.accept(t);
        }
    }

    default void isSatisfiedBy(T t, Runnable isSatisfied, Consumer<T> isNotSatisfied) {
        if (isSatisfiedBy(t)) {
            isSatisfied.run();
        } else {
            isNotSatisfied.accept(t);
        }
    }

    default void isSatisfiedBy(T t, Consumer<T> isSatisfied, Runnable isNotSatisfied) {
        if (isSatisfiedBy(t)) {
            isSatisfied.accept(t);
        } else {
            isNotSatisfied.run();
        }
    }
}
