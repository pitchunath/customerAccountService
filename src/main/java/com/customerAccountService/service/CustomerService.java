package com.customerAccountService.service;


import com.customerAccountService.error.exception.CustomerNotFoundException;
import com.customerAccountService.mappers.GetResponseMappers;
import com.customerAccountService.model.entity.Customer;
import com.customerAccountService.model.CustomerRequestParams;
import com.customerAccountService.model.dtos.CustomerDTO;
import com.customerAccountService.model.dtos.CustomerRequestDTO;
import com.customerAccountService.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.customerAccountService.mappers.GetResponseMappers.convertToCustomerDTO;
import static com.customerAccountService.mappers.PostRequestMappers.convertToCustomer;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ValidationService validationService;
    private final EntityManager entityManager;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ValidationService validationService, EntityManager entityManager) {
        this.customerRepository = customerRepository;
        this.validationService = validationService;
        this.entityManager = entityManager;
    }
    public Customer createCustomer(CustomerRequestDTO customer) {
        return customerRepository.save(convertToCustomer(customer, null));
    }

    public Customer updateCustomer(Long customerId, CustomerRequestDTO customerDetails) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return customerRepository.save(convertToCustomer(customerDetails, customer));
    }

    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        validationService.validate("deleteCustomerValidationHandler", convertToCustomerDTO(customer));
        customerRepository.deleteById(customerId);
    }

    public CustomerDTO getCustomerDetails(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return convertToCustomerDTO(customer);
    }


    public List<CustomerDTO> getAllCustomersWithFilters(CustomerRequestParams requestParams) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);
        List<Predicate> predicates = new ArrayList<>();
        if (requestParams.age() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("age"), requestParams.age()));
        }
        if (requestParams.type() != null) {
            predicates.add(cb.equal(root.get("type"), requestParams.type()));
        }
        query.where(predicates.toArray(new Predicate[0]));

        List<Customer> customers = entityManager.createQuery(query).getResultList();
        return customers.stream().map(GetResponseMappers::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

}
