package com.customerAccountService.controller;


import com.customerAccountService.error.ErrorResponse;
import com.customerAccountService.model.entity.Customer;
import com.customerAccountService.model.CustomerRequestParams;
import com.customerAccountService.model.dtos.CustomerDTO;
import com.customerAccountService.model.dtos.CustomerRequestDTO;
import com.customerAccountService.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created a new customer", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerRequestDTO customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
    }

    @Operation(summary = "update customer details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully updated customer details", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "CUSTOMER NOT FOUND", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/customers/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long customerId, @RequestBody CustomerRequestDTO customerDetails) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerDetails));
    }

    @Operation(summary = "delete a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted Customer", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "CUSTOMER NOT FOUND", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "UN PROCESSABLE ENTITY", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "get all customer details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully fetched all customer details", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(CustomerRequestParams params) {
        return ResponseEntity.ok(customerService.getAllCustomersWithFilters(params));
    }

    @Operation(summary = "get Customer details by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully fetched customer details", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "CUSTOMER NOT FOUND", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerDetails(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerDetails(customerId));
    }
}
