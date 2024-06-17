package com.customerAccountService.model;

import com.customerAccountService.model.dtos.CustomerType;
import org.springframework.web.bind.annotation.RequestParam;

public record CustomerRequestParams(
        @RequestParam(required = false)
        Integer age,
        @RequestParam(required = false)
        CustomerType type
) {
}
