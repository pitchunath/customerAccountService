package com.customerAccountService.service;



import com.customerAccountService.error.ValidationResult;
import com.customerAccountService.error.exception.ValidationRuleFailureException;
import com.customerAccountService.validation.ValidationHandler;

import java.util.List;
import java.util.Map;

public class ValidationService {

    private final Map<String, ValidationHandler<?>> handlers;

    public ValidationService(Map<String, ValidationHandler<?>> handlers) {
        this.handlers = handlers;
    }

    public <T> void validate(String handlerName, T request) {
        @SuppressWarnings("unchecked")
        ValidationHandler<T> handler = (ValidationHandler<T>) handlers.get(handlerName);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for name: " + handlerName);
        }
        List<ValidationResult> validationResults = handler.validate(request);
        if(!validationResults.isEmpty()) {
            throw new ValidationRuleFailureException("Validation failure", validationResults);
        }
    }
}
