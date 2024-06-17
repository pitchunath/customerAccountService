package com.customerAccountService.mappers;

import com.customerAccountService.model.entity.Account;
import com.customerAccountService.model.entity.Customer;
import com.customerAccountService.model.dtos.AccountDTO;
import com.customerAccountService.model.dtos.CustomerDTO;

import java.util.stream.Collectors;


public class GetResponseMappers {
    public static CustomerDTO convertToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setAge(customer.getAge());
        customerDTO.setType(customer.getType());
        customerDTO.setAccounts(customer.getAccounts().stream().map(GetResponseMappers::convertToAccountDTO).collect(Collectors.toList()));
        return customerDTO;
    }

    public static AccountDTO convertToAccountDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setStatus(account.getStatus());
        return accountDTO;
    }
}
