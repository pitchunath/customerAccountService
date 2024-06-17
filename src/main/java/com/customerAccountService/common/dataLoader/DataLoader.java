package com.customerAccountService.common.dataLoader;


import com.customerAccountService.model.dtos.AccountRequestDTO;
import com.customerAccountService.model.dtos.CustomerRequestDTO;
import com.customerAccountService.service.AccountService;
import com.customerAccountService.service.CustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;



    @Override
    public void run(ApplicationArguments args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CustomerRequestDTO> customers = objectMapper.readValue(
                new ClassPathResource("/dataloader/customerDtos.json").getFile(),
                new TypeReference<>() {
                }
        );
        List<AccountRequestDTO> accounts = objectMapper.readValue(
                new ClassPathResource("/dataloader/accountDtos.json").getFile(),
                new TypeReference<>() {
                }
        );

        customers.forEach(customerRequestDTO ->
                customerService.createCustomer(customerRequestDTO)
        );
        accounts.forEach( accountDTO ->
                accountService.createAccount(1L,accountDTO)
        );

    }
}
