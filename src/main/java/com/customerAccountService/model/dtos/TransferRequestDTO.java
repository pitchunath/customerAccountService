package com.customerAccountService.model.dtos;


import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequestDTO(
        @NotNull
        Long fromAccountId,
        @NotNull
        Long toAccountId,
        @NotNull
        BigDecimal amount
) {
}

