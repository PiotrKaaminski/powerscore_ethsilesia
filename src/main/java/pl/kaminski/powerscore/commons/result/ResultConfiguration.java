package pl.kaminski.powerscore.commons.result;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OperationService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResultConfiguration {

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnClass(GenericResponseService.class)
    CustomSwaggerResponseService customSwaggerResponseService(OperationService operationService, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils) {
        return new CustomSwaggerResponseService(operationService, springDocConfigProperties, propertyResolverUtils);
    }

    @Bean
    @ConditionalOnWebApplication
    ResultResponseBodyAdvice resultResponseBodyAdvice() {
        return new ResultResponseBodyAdvice();
    }
}
