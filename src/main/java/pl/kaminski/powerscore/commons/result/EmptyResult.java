package pl.kaminski.powerscore.commons.result;

import lombok.ToString;

@ToString
public class EmptyResult<E extends ResultError> {

    private static final EmptyResult<?> SUCCESS = new EmptyResult<>();

    private final E error;
    private final boolean isSuccess;

    protected EmptyResult() {
        this.error = null;
        this.isSuccess = true;
    }

    protected EmptyResult(E error) {
        assert error != null : "error object cannot be null";
        this.error = error;
        this.isSuccess = false;
    }

    public static <E extends ResultError> EmptyResult<E> error(E error) {
        assert error != null : "error object cannot be null";
        return new EmptyResult<>(error);
    }

    public static <E extends ResultError> EmptyResult<E> success() {
        return (EmptyResult<E>) SUCCESS;
    }

    public boolean isSuccess() {return isSuccess;}

    public boolean isError() {return !isSuccess;}

    private void assertIsError() {
        if (isSuccess()) {
            throw new NullPointerException("Result object is not reason");
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
