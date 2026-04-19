package pl.kaminski.powerscore.commons.result;

import java.util.function.Consumer;

public class Result<S, E extends ResultError> {

    private final S success;
    private final E error;
    private final boolean isSuccess;

    protected Result(S success) {
        assert success != null : "success object cannot be null";
        this.success = success;
        this.error = null;
        this.isSuccess = true;
    }

    protected Result(E error) {
        assert error != null : "error object cannot be null";
        this.error = error;
        this.isSuccess = false;
        this.success = null;
    }

    public static <S, E extends ResultError> Result<S, E> error(E error) {
        return new Result<>(error);
    }

    public static <S, E extends ResultError> Result<S, E> success(S success) {
        return new Result<>(success);
    }

    public S getSuccess() {
        assertIsSuccess();
        return success;
    }

    public boolean isSuccess() {return isSuccess;}

    public boolean isError() {return !isSuccess;}

    public void handle(Consumer<S> successConsumer, Consumer<E> errorConsumer) {
        if (isSuccess) {
            successConsumer.accept(success);
        } else {
            errorConsumer.accept(error);
        }
    }

    private void assertIsError() {
        if (isSuccess()) {
            throw new NullPointerException("Result object is not error");
        }
    }

    public E getError() {
        assertIsError();
        return error;
    }

    protected void assertIsSuccess() {
        if (isError()) {
            throw new NullPointerException("Result object is not success");
        }
    }

}
