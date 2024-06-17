package com.customerAccountService.model.dtos;


import jakarta.validation.constraints.NotNull;

public record CustomerRequestDTO(
        @NotNull
        String name,
        @NotNull
        String email,
        @NotNull
        int age,
        @NotNull
        CustomerType type
) {
}

