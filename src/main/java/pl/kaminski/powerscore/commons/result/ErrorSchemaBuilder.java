package pl.kaminski.powerscore.commons.result;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@Slf4j
class ErrorSchemaBuilder {

    ApiResponses buildApiResponsesForError(MethodParameter returnType) {
        Map<HttpStatus, Set<Class<?>>> statusToErrorTypesMap = getErrorTypesPerHttpStatus(returnType);
        var apiResponses = new ApiResponses();
        for (var entry : statusToErrorTypesMap.entrySet()) {
            var httpStatus = entry.getKey();
            var schema = createApiResponseForErrorTypes(entry.getValue());

            var mediaType = new MediaType();
            mediaType.setSchema(schema);

            var content = new Content();
            content.put("*/*", mediaType);

            var apiResponse = new ApiResponse();
            apiResponse.setContent(content);
            apiResponse.setDescription(httpStatus.getReasonPhrase());
            apiResponses.put(String.valueOf(httpStatus.value()), apiResponse);
        }
        return apiResponses;
    }

    private Map<HttpStatus, Set<Class<?>>> getErrorTypesPerHttpStatus(MethodParameter returnType) {
        // pogrupowanie struktur błędu po http status code (jesli się da, tzn. jesli podtyp jest finalem, enumem lub recordem - wtedy 1 struktura, lub sealed - wtedy wszystkie możliwe podtypy tego typu sealed)
        // jeśli dla jakiegoś typu nie da się wyciągnąć podtypów (np. klasa non final albo klasa/interfejs które nie są sealed) wtedy generujemy strukturę tylko dla tego konkretnego typu + warning z informacją że nie da się wydedukować podtypów
        Class<?> errorType = getErrorType(returnType);
        if (errorType.isRecord() || errorType.isEnum() || Modifier.isFinal(errorType.getModifiers())) {
            return Map.of(getStatusForError(errorType), Set.of(errorType));
        }
        if (!errorType.isSealed()) {
            log.warn("{} is not sealed, cannot infer all possible subtypes, only original error type will be considered for schema generation", errorType.getCanonicalName());
            return Map.of(getStatusForError(errorType), Set.of(errorType));
        }
        var result = new HashMap<HttpStatus, Set<Class<?>>>();
        for (var clazz : errorType.getPermittedSubclasses()) {
            if (!Modifier.isFinal(clazz.getModifiers())) {
                log.warn("subtype {} of error {} is non-selaed, cannot infer possible error subtypes for class {}", clazz.getCanonicalName(), errorType.getCanonicalName(), clazz.getCanonicalName());
            }
            var errorStatus = getStatusForError(clazz);
            if (!result.containsKey(errorStatus)) result.put(errorStatus, new HashSet<>());
            result.get(errorStatus).add(clazz);
        }
        return result;
    }

    private Class<?> getErrorType(MethodParameter returnType) {
        var resultType = returnType.getParameterType();
        if (Result.class.equals(resultType)) {
            return (Class<?>) ((ParameterizedType) returnType.getGenericParameterType()).getActualTypeArguments()[1];
        } else if (EmptyResult.class.equals(resultType)) {
            return (Class<?>) ((ParameterizedType) returnType.getGenericParameterType()).getActualTypeArguments()[0];
        }
        var errorClass = returnType.getParameterType();
        while (!errorClass.getSuperclass().equals(Result.class) && !errorClass.getSuperclass().equals(EmptyResult.class)) {
            errorClass = errorClass.getSuperclass();
        }
        if (errorClass.getSuperclass().equals(Result.class)) {
            return (Class<?>) ((ParameterizedType) errorClass.getGenericSuperclass()).getActualTypeArguments()[1];
        } else {
            return (Class<?>) ((ParameterizedType) errorClass.getGenericSuperclass()).getActualTypeArguments()[0];
        }
    }

    private HttpStatus getStatusForError(Class<?> clazz) {
        ResponseStatus annotation = AnnotatedElementUtils.findMergedAnnotation(clazz, ResponseStatus.class);
        return annotation == null ? HttpStatus.BAD_REQUEST : annotation.value();
    }

    private Schema createApiResponseForErrorTypes(Set<Class<?>> errorTypes) {
        // generowanie struktur błedu w taki sposób, aby errorCode zawierał listę (jeśłi podtypem jest enum) lub konkretną wartość (jeśli podtypem jest klasa)
        // odnaleźć sruktury simpleErrorSchema (takie które mają tylko 2 pola, errorCode i message
        // dla struktur simpleErrorSchema zagregować możliwe kody błędów
        // struktury complexErrorSchema zapisać do kolekcji
//         ModelConverters.getInstance().readAllAsResolvedSchema(false, Schema.SchemaResolution.INLINE);
        var simpleSchemaErrorCodes = new TreeSet<String>();
        var complexSchemas = new HashSet<Schema>();
        for (var type : errorTypes) {
            if (Enum.class.isAssignableFrom(type)) {
                simpleSchemaErrorCodes.addAll(ResultUtils.getErrorCodes(type));
                continue;
            }
//            var schema = ModelConverters.getInstance(true, Schema.SchemaResolution.INLINE).readAllAsResolvedSchema(type).schema;
            var schema = ModelConverters.getInstance(false, Schema.SchemaResolution.INLINE).readAllAsResolvedSchema(type).schema;
            if (isSimpleSchema(schema)) {
                simpleSchemaErrorCodes.addAll(ResultUtils.getErrorCodes(type));
            } else {
                complexSchemas.add(normalizeComplexSchema(schema, type));
            }
        }
        // jeśli jest kilka struktur (np simpleErrorSchema i complexErrorSchema, albo kilka complexErrorSchema) to konstruujemy schemat z properties errorCode(enum), message i oneOf(rozpoznane schematy błedów)
        if (shouldMergeSchemas(simpleSchemaErrorCodes, complexSchemas)) {
            var schema = new ObjectSchema();
            schema.addProperty("message", createMessageSchema());
            if (!simpleSchemaErrorCodes.isEmpty()) {
                var simpleErrorSchema = new ObjectSchema();
                simpleErrorSchema.addProperty("errorCode", createErrorCodesSchema(simpleSchemaErrorCodes));
                schema.addOneOfItem(simpleErrorSchema);
            }
            for (var complexSchema : complexSchemas) {
                complexSchema.getProperties().remove("message");
                schema.addOneOfItem(complexSchema);
            }
            return schema;
        }
        // jeśli w wyniku mamy tylko 1 rodzaj struktury (samo simpleErrorSchema lub complexErrorSchema) to zwracamy to jako schemat dla danego statusu http
        if (!simpleSchemaErrorCodes.isEmpty()) {
            var simpleErrorSchema = new ObjectSchema();
            simpleErrorSchema.addProperty("message", createMessageSchema());
            simpleErrorSchema.addProperty("errorCode", createErrorCodesSchema(simpleSchemaErrorCodes));
            return simpleErrorSchema;
        }
        return complexSchemas.iterator().next();
    }

    private boolean shouldMergeSchemas(TreeSet<String> simpleSchemaErrorCodes, HashSet<Schema> complexSchemas) {
        return complexSchemas.size() >= 2 || (!simpleSchemaErrorCodes.isEmpty() && !complexSchemas.isEmpty());
    }

    private Schema normalizeComplexSchema(Schema schema, Class<?> type) {
        var errorCodes = ResultUtils.getErrorCodes(type);
        var properties = schema.getProperties();
        properties.put("errorCode", createErrorCodesSchema(errorCodes));
        return schema;
    }

    private boolean isSimpleSchema(Schema schema) {
        var properties = schema.getProperties();
        return properties.size() == 2 && properties.containsKey("errorCode") && properties.containsKey("message");
    }

    private StringSchema createErrorCodesSchema(Collection<String> errorCodes) {
        var schema = new StringSchema();
        schema.setEnum(new ArrayList<>(errorCodes));
        return schema;
    }

    private StringSchema createMessageSchema() {
        return new StringSchema();
    }
}
