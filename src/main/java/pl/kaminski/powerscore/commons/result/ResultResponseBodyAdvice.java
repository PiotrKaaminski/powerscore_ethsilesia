package pl.kaminski.powerscore.commons.result;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
class ResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Result.class.isAssignableFrom(returnType.getParameterType()) || EmptyResult.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Result<?, ?> result) {
            if (result.isSuccess()) {

                return result.getSuccess();
            } else {
                sendErrorCode(response, result.getError());
                return buildError(result.getError());
            }
        } else if (body instanceof EmptyResult<?> emptyResult) {
            if (emptyResult.isSuccess()) {
                return null;
            } else {
                sendErrorCode(response, emptyResult.getError());
                return buildError(emptyResult.getError());
            }
        } else {
            throw new IllegalStateException("body is neither Result nor EmptyResult");
        }
    }

    private void sendErrorCode(ServerHttpResponse response, ResultError errorResult) {
        // dopracować przypisywanie response status, skorzystać ze springowej klasy utils od @AliasFor
        var responseStatus = errorResult.getClass().getDeclaredAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            response.setStatusCode(responseStatus.value());
        } else {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
        }
    }

    private ResultError buildError(ResultError error) {
        if (error instanceof Enum<?> enumError) {
            return new EnumResultError(enumError, error.getMessage());
        }
        return error;
    }

    public record EnumResultError<E extends Enum<E>>(
            E errorCode,
            String message
    ) implements ResultError {

        @Override
        public String getErrorCode() {
            return errorCode.name();
        }

        public String getMessage() {
            return message;
        }
    }

}
