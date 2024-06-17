package com.customerAccountService.model.dtos;

import com.customerAccountService.model.AccountStatus;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountRequestDTO (
        @NotNull
        BigDecimal balance,
         @NotNull
         AccountStatus status
){

}
