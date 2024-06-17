package com.customerAccountService.validation;


import com.customerAccountService.error.ValidationResult;
import com.customerAccountService.model.AccountStatus;
import com.customerAccountService.model.dtos.CustomerDTO;
import com.customerAccountService.model.validation.TransferValidationParameters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class TranferAmountValidationHandler implements ValidationHandler<TransferValidationParameters> {

    @Override
    public List<ValidationResult> validate(TransferValidationParameters request) {
        List<ValidationResult> validationResults = new ArrayList<>();

        // Define validation rules
        validateRule(validationResults,
                request,
                (transfer -> transfer.fromAccount().getStatus() != AccountStatus.ACTIVE
                        || transfer.toAccount().getStatus() != AccountStatus.ACTIVE),
                "CAS0003"
        );
        validateRule(validationResults, request,
                transfer -> transfer.amount().compareTo(BigDecimal.ZERO) <= 0,
                "CAS0004"
        );
        return validationResults;
    }

    private void validateRule(List<ValidationResult> validationResults,
                              TransferValidationParameters request,
                              Predicate<TransferValidationParameters> predicate, String ruleKey) {
        if (predicate.test(request)) {

            validationResults.add(new ValidationResult(ruleKey, getMessage(ruleKey)));
        }
    }
}
