package com.customerAccountService.error.exception;

import com.customerAccountService.error.ValidationResult;

import java.util.List;

public class ValidationRuleFailureException extends RuntimeException{
    private final List<ValidationResult> bindingErrors;

    public ValidationRuleFailureException(String message, List<ValidationResult> bindingErrors) {
        super(message);
        this.bindingErrors = bindingErrors;
    }

    public List<ValidationResult> getBindingErrors() {
        return bindingErrors;
    }
}
