package com.customerAccountService.model.validation;

import com.customerAccountService.model.dtos.AccountDTO;

import java.math.BigDecimal;

public record TransferValidationParameters(
        AccountDTO fromAccount,
        AccountDTO toAccount,
        BigDecimal amount
) {

}
