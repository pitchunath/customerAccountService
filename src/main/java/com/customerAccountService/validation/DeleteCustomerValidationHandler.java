package com.customerAccountService.validation;


import com.customerAccountService.error.ValidationResult;
import com.customerAccountService.model.AccountStatus;
import com.customerAccountService.model.dtos.CustomerDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class DeleteCustomerValidationHandler implements ValidationHandler<CustomerDTO> {

    @Override
    public List<ValidationResult> validate(CustomerDTO request) {
        List<ValidationResult> validationResults = new ArrayList<>();

        // Define validation rules
        validateRule(validationResults, request,
                customer -> customer.getAccounts().stream().anyMatch(account -> account.getStatus() == AccountStatus.ACTIVE),
                "CAS0001"
        );
        validateRule(validationResults, request,
                customer -> customer.getAccounts().stream().anyMatch(account -> account.getBalance().compareTo(BigDecimal.ZERO) > 0),
                "CAS0002"
        );
        return validationResults;
    }

    private void validateRule(List<ValidationResult> validationResults,
                              CustomerDTO request,
                              Predicate<CustomerDTO> predicate, String ruleKey) {
        if (predicate.test(request)) {

            validationResults.add(new ValidationResult(ruleKey, getMessage(ruleKey)));
        }
    }
}
