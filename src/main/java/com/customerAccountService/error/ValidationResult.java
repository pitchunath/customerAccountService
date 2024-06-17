package com.customerAccountService.error;

public record ValidationResult(
        String code,
        String message
){}
