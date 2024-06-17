package com.customerAccountService.configuration;


import com.customerAccountService.service.ValidationService;
import com.customerAccountService.validation.DeleteAccountValidationHandler;
import com.customerAccountService.validation.DeleteCustomerValidationHandler;
import com.customerAccountService.validation.TranferAmountValidationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ValidationConfig {

    @Bean
    public ValidationService validationService() {
        return new ValidationService(Map.of(
                "deleteCustomerValidationHandler", new DeleteCustomerValidationHandler(),
                "deleteAccountValidationHandler", new DeleteAccountValidationHandler(),
                "transferAmountValidationHandler", new TranferAmountValidationHandler()
        )
        );
    }
}
