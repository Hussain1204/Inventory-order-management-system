package com.hussain.inventory.service;

import com.hussain.inventory.dto.customer.CustomerRequest;
import com.hussain.inventory.dto.customer.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);
    CustomerResponse getById(Long id);
    Page<CustomerResponse> getAll(Pageable pageable);
}
