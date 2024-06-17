package com.customerAccountService.integrationTest;

import com.customerAccountService.model.entity.Account;
import com.customerAccountService.model.AccountStatus;
import com.customerAccountService.model.entity.Customer;
import com.customerAccountService.repository.AccountRepository;
import com.customerAccountService.repository.CustomerRepository;
import com.customerAccountService.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.customerAccountService.model.dtos.CustomerType.BASIC;

public class BaseIntegrationTest {
    @LocalServerPort
    private int port;

    public String baseUrl;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api";
        testDataSetup();

    }
    public static HttpHeaders getAdminHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        return headers;
    }

    public static HttpHeaders getUserHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "user");
        return headers;
    }

    public void testDataSetup(){
       Customer customer1 =customerRepository.save(new Customer("customer1","customer1@testdomain.com", 18,BASIC,new ArrayList<>()));
       Customer customer2 =customerRepository.save(new Customer("customer2","customer2@testdomain.com",18,BASIC,new ArrayList<>()));
        Customer customer3 =customerRepository.save(new Customer("customer3","customer3@testdomain.com",18,BASIC,new ArrayList<>()));
       accountRepository.save(new Account(new BigDecimal(5000), AccountStatus.ACTIVE,customer1));
       accountRepository.save(new Account(new BigDecimal(6000),AccountStatus.ACTIVE,customer2));
       accountRepository.save(new Account(new BigDecimal(0),AccountStatus.CLOSED,customer3));
    }
}
