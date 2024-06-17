package com.customerAccountService.controller;


import com.customerAccountService.error.ErrorResponse;
import com.customerAccountService.model.dtos.AccountDTO;
import com.customerAccountService.model.dtos.AccountRequestDTO;
import com.customerAccountService.model.dtos.TransferRequestDTO;
import com.customerAccountService.service.AccountService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "create a new account for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created a new account for a customer", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/customers/{customerId}/accounts")
    public ResponseEntity<AccountDTO> createAccount(@PathVariable Long customerId, @Valid @RequestBody AccountRequestDTO account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(customerId, account));
    }

    @Operation(summary = "transfer amount between two accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "transaction successfully completed", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "UN PROCESSABLE ENTITY", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/transfer")
    public ResponseEntity<Void> transferMoney(@Valid @RequestBody TransferRequestDTO transferRequest) {
        accountService.transferMoney(transferRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "delete a account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfully deleted account", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "ACCOUNT NOT FOUND", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "UN PROCESSABLE ENTITY", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }

}
