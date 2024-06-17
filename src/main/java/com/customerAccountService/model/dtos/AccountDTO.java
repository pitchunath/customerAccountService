package com.customerAccountService.model.dtos;

import com.customerAccountService.model.AccountStatus;

import java.math.BigDecimal;

public class AccountDTO {
    private Long id;
    private BigDecimal balance;
    private AccountStatus status;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
