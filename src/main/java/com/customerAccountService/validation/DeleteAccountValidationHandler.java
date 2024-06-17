package com.customerAccountService.validation;


import com.customerAccountService.error.ValidationResult;
import com.customerAccountService.model.AccountStatus;
import com.customerAccountService.model.dtos.AccountDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class DeleteAccountValidationHandler implements ValidationHandler<AccountDTO> {

    @Override
    public List<ValidationResult> validate(AccountDTO request) {
        List<ValidationResult> validationResults = new ArrayList<>();


        validateRule(
                validationResults,
                request,
                account -> account.getStatus() == AccountStatus.ACTIVE, "CAS0005"
        );
        validateRule(
                validationResults,
                request,
                account -> account.getBalance().compareTo(BigDecimal.ZERO) > 0, "CAS0006"
        );
        return validationResults;
    }

    private void validateRule(
            List<ValidationResult> validationResults,
            AccountDTO request, Predicate<AccountDTO> predicate,
            String ruleKey) {
        if (predicate.test(request)) {

            validationResults.add(new ValidationResult(ruleKey, getMessage(ruleKey)));
        }
    }
}
