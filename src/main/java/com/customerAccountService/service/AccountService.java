package com.customerAccountService.service;


import com.customerAccountService.error.exception.AccountNotFoundException;
import com.customerAccountService.error.exception.CustomerNotFoundException;
import com.customerAccountService.error.exception.InsufficientBalanceException;
import com.customerAccountService.model.entity.Account;
import com.customerAccountService.model.entity.Customer;
import com.customerAccountService.model.dtos.AccountDTO;
import com.customerAccountService.model.dtos.AccountRequestDTO;
import com.customerAccountService.model.dtos.TransferRequestDTO;
import com.customerAccountService.model.validation.TransferValidationParameters;
import com.customerAccountService.repository.AccountRepository;
import com.customerAccountService.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.customerAccountService.mappers.GetResponseMappers.convertToAccountDTO;
import static com.customerAccountService.mappers.PostRequestMappers.convertToAccount;

@Service
public class AccountService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final ValidationService validationService;

    @Autowired
    public AccountService(CustomerRepository customerRepository, AccountRepository accountRepository, ValidationService validationService) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.validationService = validationService;
    }


    public AccountDTO createAccount(Long customerId, AccountRequestDTO account) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        Account newAccount = convertToAccount(account, null);
        newAccount.setCustomer(customer);
        return convertToAccountDTO(accountRepository.save(newAccount));
    }

    @Transactional
    public void transferMoney(TransferRequestDTO transferRequest) {
        Account fromAccount = accountRepository.findById(transferRequest.fromAccountId()).orElseThrow(() -> new AccountNotFoundException("Account not found"));
        Account toAccount = accountRepository.findById(transferRequest.toAccountId()).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (fromAccount.getBalance().compareTo(transferRequest.amount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        validationService.validate("transferAmountValidationHandler", new TransferValidationParameters(
                convertToAccountDTO(fromAccount),
                convertToAccountDTO(toAccount),
                transferRequest.amount()
        ));

        fromAccount.setBalance(fromAccount.getBalance().subtract(transferRequest.amount()));
        toAccount.setBalance(toAccount.getBalance().add(transferRequest.amount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }


    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        validationService.validate("deleteAccountValidationHandler", convertToAccountDTO(account));
        accountRepository.deleteById(accountId);
    }


}
