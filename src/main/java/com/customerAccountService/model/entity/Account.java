package com.customerAccountService.model.entity;

import com.customerAccountService.model.AccountStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal balance;
    private AccountStatus status;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Default constructor
    public Account(AccountStatus status) {
        this.status = status;
    }

    // Constructor with parameters
    public Account(BigDecimal balance, AccountStatus status, Customer customer) {
        this.balance = balance;
        this.status = status;
        this.customer = customer;
    }

    public Account() {

    }

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}

