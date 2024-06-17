package com.customerAccountService.service;

import com.customerAccountService.error.ValidationResult;
import com.customerAccountService.error.exception.ValidationRuleFailureException;
import com.customerAccountService.validation.ValidationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

    @Mock
    private ValidationHandler<Object> validationHandler;

    @InjectMocks
    private ValidationService validationService;

    private static final String HANDLER_NAME = "testHandler";

    @BeforeEach
    void setUp() {
        // Initialize the validation service with a mock handler
        validationService = new ValidationService(Map.of(HANDLER_NAME, validationHandler));
    }

    @Test
    void testValidate_Success() {
        Object request = new Object();

        when(validationHandler.validate(request)).thenReturn(Collections.emptyList());

        // This should not throw any exception
        assertDoesNotThrow(() -> validationService.validate(HANDLER_NAME, request));

        verify(validationHandler).validate(request);
    }

    @Test
    void testValidate_ValidationFailure() {
        Object request = new Object();
        ValidationResult validationResult = new ValidationResult("error", "field");
        when(validationHandler.validate(request)).thenReturn(List.of(validationResult));

        ValidationRuleFailureException exception = assertThrows(
                ValidationRuleFailureException.class,
                () -> validationService.validate(HANDLER_NAME, request)
        );

        assertEquals("Validation failure", exception.getMessage());
        assertEquals(1, exception.getBindingErrors().size());
        assertEquals(validationResult, exception.getBindingErrors().get(0));

        verify(validationHandler).validate(request);
    }

    @Test
    void testValidate_HandlerNotFound() {
        Object request = new Object();
        String invalidHandlerName = "invalidHandler";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validate(invalidHandlerName, request)
        );

        assertEquals("No handler found for name: " + invalidHandlerName, exception.getMessage());

        verify(validationHandler, never()).validate(any());
    }
}
