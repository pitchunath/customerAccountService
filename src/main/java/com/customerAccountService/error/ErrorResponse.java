package com.customerAccountService.error;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(
        @Schema(description = "Error message", example = "Error message")
        String message,
        @Schema(description = "Http status message", example = "Http status message")
        HttpStatus httpStatus,
        @Schema(description = "list of validation errors")
         List<ValidationResult> bindingErrors
) {


        public static ErrorResponse of(HttpStatus status, String message,List<ValidationResult> bindingErrors) {
                return new ErrorResponse(
                        message,
                        status,
                        bindingErrors != null ? bindingErrors : List.of()
                );
        }



}
