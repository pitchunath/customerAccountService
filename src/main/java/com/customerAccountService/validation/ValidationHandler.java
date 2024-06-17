package com.customerAccountService.validation;

import com.customerAccountService.error.ErrorResponse;
import com.customerAccountService.error.ValidationResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ValidationHandler<T> {
    List<ValidationResult> validate(T request);
    Map<String, ValidationResult> MESSAGES = loadMessages();


    static Map<String, ValidationResult> loadMessages() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ValidationResult> validationResults = objectMapper.readValue(
                    new ClassPathResource("/validation/validation-messages.json").getFile(),
                    new TypeReference<>() {
                    }
            );
            Map<String, ValidationResult> messageMap = new HashMap<>();
            validationResults.forEach( validationResult ->
                    messageMap.put(validationResult.code(), validationResult)
            );

            return messageMap;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load validation messages", e);
        }
    }

    default String getMessage(String ruleKey) {
        return MESSAGES.get(ruleKey).message();
    }
}
