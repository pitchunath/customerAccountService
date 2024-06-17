package com.customerAccountService.service;

import com.customerAccountService.error.exception.CustomerNotFoundException;
import com.customerAccountService.model.entity.Customer;
import com.customerAccountService.model.CustomerRequestParams;
import com.customerAccountService.model.dtos.CustomerDTO;
import com.customerAccountService.model.dtos.CustomerRequestDTO;
import com.customerAccountService.model.dtos.CustomerType;
import com.customerAccountService.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerRequestDTO customerRequestDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setType(CustomerType.BASIC);
        customer.setAge(18);

        customerRequestDTO = new CustomerRequestDTO("John Doe", "john@example.com", 18, CustomerType.BASIC);
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.createCustomer(customerRequestDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.updateCustomer(1L, customerRequestDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(1L, customerRequestDTO));

        verify(customerRepository).findById(1L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository).findById(1L);
        verify(validationService).validate(anyString(), any(CustomerDTO.class));
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void testDeleteCustomer_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(1L));

        verify(customerRepository).findById(1L);
        verify(validationService, never()).validate(anyString(), any(CustomerDTO.class));
        verify(customerRepository, never()).deleteById(1L);
    }

    @Test
    void testGetCustomerDetails() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDTO result = customerService.getCustomerDetails(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(customerRepository).findById(1L);
    }

    @Test
    void testGetCustomerDetails_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerDetails(1L));

        verify(customerRepository).findById(1L);
    }

    @Test
    void testGetAllCustomers() {
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Doe");
        customer2.setEmail("jane@example.com");
        customer2.setAge(18);
        customer2.setType(CustomerType.BASIC);

        // Mocking EntityManager behavior
        CriteriaBuilder criteriaBuilderMock = mock(CriteriaBuilder.class);
        CriteriaQuery<Customer> criteriaQueryMock = mock(CriteriaQuery.class);
        Root<Customer> rootMock = mock(Root.class);
        TypedQuery<Customer> typedQueryMock = mock(TypedQuery.class); // mock typed query

        // Return mock TypedQuery when entityManager.createQuery() is called
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilderMock);
        when(criteriaBuilderMock.createQuery(Customer.class)).thenReturn(criteriaQueryMock);
        when(criteriaQueryMock.from(Customer.class)).thenReturn(rootMock);
        when(entityManager.createQuery(criteriaQueryMock)).thenReturn(typedQueryMock); // return mock TypedQuery

        // Mock behavior of TypedQuery to return a list or handle further interactions
        when(typedQueryMock.getResultList()).thenReturn(Arrays.asList(customer,customer2));


        List<CustomerDTO> result = customerService.getAllCustomersWithFilters(new CustomerRequestParams(null, null));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("jane@example.com", result.get(1).getEmail());
    }
}
