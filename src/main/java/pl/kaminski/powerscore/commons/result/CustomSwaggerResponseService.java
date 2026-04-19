package pl.kaminski.powerscore.commons.result;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.MethodAttributes;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OperationService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
class CustomSwaggerResponseService extends GenericResponseService {

    private final ErrorSchemaBuilder errorSchemaBuilder = new ErrorSchemaBuilder();

    public CustomSwaggerResponseService(OperationService operationService, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils) {
        super(operationService, springDocConfigProperties, propertyResolverUtils);
    }

    @Override
    public ApiResponses build(Components components, HandlerMethod handlerMethod, Operation operation, MethodAttributes methodAttributes) {
        // sprawdzić czy metoda zwraca Result, jesli nie to return super
        var returnType = handlerMethod.getReturnType().getParameterType();
        if (Result.class.isAssignableFrom(returnType) || EmptyResult.class.isAssignableFrom(returnType)) {
            return resolveForResultType(components, handlerMethod, operation, methodAttributes);
        }
        return super.build(components, handlerMethod, operation, methodAttributes);
    }

    private ApiResponses resolveForResultType(Components components, HandlerMethod handlerMethod, Operation operation, MethodAttributes methodAttributes) {
        var apiResponses = getApiResponsesForSuccess(components, handlerMethod, operation, methodAttributes);
        errorSchemaBuilder.buildApiResponsesForError(handlerMethod.getReturnType())
                .forEach(apiResponses::addApiResponse);
        return apiResponses;
    }

    private ApiResponses getApiResponsesForSuccess(Components components, HandlerMethod handlerMethod, Operation operation, MethodAttributes methodAttributes) {
        var resultType = handlerMethod.getReturnType().getParameterType();
        if (EmptyResult.class.isAssignableFrom(resultType)) {
            // use super.build against Void.class to handle response statuses and other spring/swagger annotation
            return super.build(components, new ConcreteResultTypeHandlerMethod(handlerMethod, Void.class), operation, methodAttributes);
        }
        Type actualSuccessType = getSuccessType(handlerMethod);
        return super.build(components, new ConcreteResultTypeHandlerMethod(handlerMethod, actualSuccessType), operation, methodAttributes);
    }

    private Type getSuccessType(HandlerMethod handlerMethod) {
        var resultType = handlerMethod.getReturnType().getParameterType();
        if (Result.class.equals(resultType)) {
            return ((ParameterizedType) handlerMethod.getReturnType().getGenericParameterType()).getActualTypeArguments()[0];
        }
        Type actualSuccessType;
        var successClass = handlerMethod.getReturnType().getParameterType();
        while (!successClass.getSuperclass().equals(Result.class)) {
            successClass = successClass.getSuperclass();
        }
        actualSuccessType = ((ParameterizedType) successClass.getGenericSuperclass()).getActualTypeArguments()[0];
        return actualSuccessType;
    }

    private static class ConcreteResultTypeHandlerMethod extends HandlerMethod {


        private final Type concreteType;

        protected ConcreteResultTypeHandlerMethod(HandlerMethod handlerMethod, Type concreteType) {
            super(handlerMethod);
            this.concreteType = concreteType;
        }

        @Override
        public MethodParameter getReturnType() {
            return new ConcreteResultTypeMethodParameter(-1);
        }


        class ConcreteResultTypeMethodParameter extends AnnotatedMethodParameter {

            public ConcreteResultTypeMethodParameter(int index) {
                super(index);
            }

            @Override
            public Type getGenericParameterType() {
                return concreteType;
            }
        }

    }

}