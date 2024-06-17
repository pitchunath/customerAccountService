package com.customerAccountService.integrationTest;

import com.customerAccountService.error.ErrorResponse;
import com.customerAccountService.error.ValidationResult;
import com.customerAccountService.model.AccountStatus;
import com.customerAccountService.model.entity.Customer;
import com.customerAccountService.model.dtos.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("integration-test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CustomerAccountServiceIntegrationTest extends BaseIntegrationTest {



    @Test
    public void testCreateCustomer() {
        String url = baseUrl + "/customers";
        HttpHeaders headers = getUserHttpHeaders();
        CustomerRequestDTO customerRequest = new CustomerRequestDTO("John Doe", "john@example.com",18,CustomerType.BASIC);
        HttpEntity<CustomerRequestDTO> request = new HttpEntity<>(customerRequest, headers);
        ResponseEntity<Customer> response = restTemplate.postForEntity(url, request, Customer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John Doe");
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    public void testCreateAccount() {
        String url = baseUrl + "/customers/1/accounts";
        HttpHeaders headers = getUserHttpHeaders();
        AccountRequestDTO accountRequest = new AccountRequestDTO( new BigDecimal("5000"), AccountStatus.ACTIVE);
        HttpEntity<AccountRequestDTO> request = new HttpEntity<>(accountRequest, headers);
        ResponseEntity<AccountDTO> response = restTemplate.postForEntity(url, request, AccountDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBalance()).isEqualByComparingTo(new BigDecimal("5000"));
    }
    @Test
    public void testCreateAccountIfNoCustomerPresent() {
        String url = baseUrl + "/customers/100/accounts";
        HttpHeaders headers = getUserHttpHeaders();
        AccountRequestDTO accountRequest = new AccountRequestDTO( new BigDecimal("5000"), AccountStatus.ACTIVE);
        HttpEntity<AccountRequestDTO> request = new HttpEntity<>(accountRequest, headers);
        ResponseEntity<AccountDTO> response = restTemplate.postForEntity(url, request, AccountDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testTransferMoney() {
        String url = baseUrl + "/transfer";

        TransferRequestDTO transferRequest = new TransferRequestDTO(1L, 2L, new BigDecimal("500"));

        HttpHeaders headers = getUserHttpHeaders();
        HttpEntity<TransferRequestDTO> request = new HttpEntity<>(transferRequest, headers);

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testDeleteAccountValidationResults() {
        String url = baseUrl + "/accounts/1";

        HttpHeaders headers = getUserHttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(url, HttpMethod.DELETE, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorResponse error  = response.getBody();
        assert error != null;
        assertThat(error.bindingErrors().stream().map(ValidationResult::code).collect(Collectors.toList())).contains("CAS0005","CAS0006");
    }

    @Test
    public void testGetCustomerDetails() {
        String url = baseUrl + "/customers/1";
        HttpHeaders headers = getUserHttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);


        ResponseEntity<CustomerDTO> response  = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                CustomerDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }
}
