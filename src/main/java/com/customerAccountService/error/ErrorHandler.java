package com.customerAccountService.error;


import com.customerAccountService.error.exception.AccountNotFoundException;
import com.customerAccountService.error.exception.CustomerNotFoundException;
import com.customerAccountService.error.exception.InsufficientBalanceException;
import com.customerAccountService.error.exception.ValidationRuleFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import javax.naming.AuthenticationException;
import java.util.Comparator;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({
            CustomerNotFoundException.class,
            AccountNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleCustomNotFoundException(Exception exception) {
        return ErrorResponse.of(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler({
            ValidationRuleFailureException.class
    })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse handleValidationFailureException(ValidationRuleFailureException exception) {
        return ErrorResponse.of(
                HttpStatus.UNPROCESSABLE_ENTITY,
                exception.getMessage(),
                exception.getBindingErrors()
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class,
            InsufficientBalanceException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleDataIntegrityViolationException(Exception exception) {
        return  ErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var errorMessage = exception.getBindingResult().getFieldErrors()
                .stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.joining(" , "));
        return ErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                errorMessage,
                null

        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleAccessDeniedException(AccessDeniedException exception) {
        return ErrorResponse.of(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleAuthenticationDeniedException(AuthenticationException exception) {
        return ErrorResponse.of(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                null
        );
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleInternalException(Exception exception) {
        return ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                null
        );
    }
}


