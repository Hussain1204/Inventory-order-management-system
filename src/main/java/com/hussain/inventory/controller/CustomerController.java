package com.hussain.inventory.controller;

import com.hussain.inventory.dto.customer.CustomerRequest;
import com.hussain.inventory.dto.customer.CustomerResponse;
import com.hussain.inventory.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @org.springframework.beans.factory.annotation.Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(customerService.getAll(pageable));
    }
}
