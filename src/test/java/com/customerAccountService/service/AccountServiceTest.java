package com.customerAccountService.service;

import com.customerAccountService.error.exception.CustomerNotFoundException;
import com.customerAccountService.error.exception.InsufficientBalanceException;
import com.customerAccountService.model.entity.Account;
import com.customerAccountService.model.AccountStatus;
import com.customerAccountService.model.entity.Customer;
import com.customerAccountService.model.dtos.AccountDTO;
import com.customerAccountService.model.dtos.AccountRequestDTO;
import com.customerAccountService.model.dtos.TransferRequestDTO;
import com.customerAccountService.model.validation.TransferValidationParameters;
import com.customerAccountService.repository.AccountRepository;
import com.customerAccountService.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private AccountService accountService;

    private Customer customer;
    private Account fromAccount;
    private Account toAccount;
    private AccountRequestDTO accountRequestDTO;
    private TransferRequestDTO transferRequestDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setBalance(new BigDecimal("1000"));

        toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(new BigDecimal("1000"));

        accountRequestDTO = new AccountRequestDTO(new BigDecimal(5000), AccountStatus.ACTIVE);
        transferRequestDTO = new TransferRequestDTO(1L, 2L, new BigDecimal("500"));
    }

    @Test
    void testCreateAccount() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);

        AccountDTO result = accountService.createAccount(1L, accountRequestDTO);

        assertNotNull(result);
        verify(customerRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void testCreateAccount_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> accountService.createAccount(1L, accountRequestDTO));

        verify(customerRepository).findById(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testTransferMoney() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        accountService.transferMoney(transferRequestDTO);

        assertEquals(new BigDecimal("500"), fromAccount.getBalance());
        assertEquals(new BigDecimal("1500"), toAccount.getBalance());

        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
        verify(validationService).validate(anyString(), any(TransferValidationParameters.class));
        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
    }

    @Test
    void testTransferMoney_InsufficientBalance() {
        transferRequestDTO = new TransferRequestDTO(1L, 2L, new BigDecimal("1500"));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(InsufficientBalanceException.class, () -> accountService.transferMoney(transferRequestDTO));

        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
        verify(validationService, never()).validate(anyString(), any(TransferValidationParameters.class));
        verify(accountRepository, never()).save(fromAccount);
        verify(accountRepository, never()).save(toAccount);
    }

    @Test
    void testDeleteAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));

        accountService.deleteAccount(1L);

        verify(accountRepository).findById(1L);
        verify(validationService).validate(anyString(), any(AccountDTO.class));
        verify(accountRepository).deleteById(1L);
    }

    @Test
    void testDeleteAccount_AccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> accountService.deleteAccount(1L));

        verify(accountRepository).findById(1L);
        verify(validationService, never()).validate(anyString(), any(AccountDTO.class));
        verify(accountRepository, never()).deleteById(1L);
    }
}
