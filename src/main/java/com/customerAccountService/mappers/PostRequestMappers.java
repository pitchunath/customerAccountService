package com.customerAccountService.mappers;

import com.customerAccountService.model.entity.Account;
import com.customerAccountService.model.entity.Customer;
import com.customerAccountService.model.dtos.AccountRequestDTO;
import com.customerAccountService.model.dtos.CustomerRequestDTO;


public class PostRequestMappers {
    public static Customer convertToCustomer(CustomerRequestDTO customerRequest,Customer customer) {
        if(customer== null){
         customer = new Customer();}
        customer.setName(customerRequest.name());
        customer.setEmail(customerRequest.email());
        customer.setAge(customerRequest.age());
        customer.setType(customerRequest.type());
        return customer;
    }

    public static Account convertToAccount(AccountRequestDTO accountRequest,Account account) {
        if(account== null){
            account = new Account();}
        account.setBalance(accountRequest.balance());
        account.setStatus(accountRequest.status());
        return account;
    }
}
