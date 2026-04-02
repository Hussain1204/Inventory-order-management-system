package com.hussain.inventory.service.impl;

import com.hussain.inventory.dto.customer.CustomerRequest;
import com.hussain.inventory.dto.customer.CustomerResponse;
import com.hussain.inventory.entity.Customer;
import com.hussain.inventory.exception.ResourceNotFoundException;
import com.hussain.inventory.repository.CustomerRepository;
import com.hussain.inventory.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @org.springframework.beans.factory.annotation.Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerResponse create(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        return mapToResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAll(Pageable pageable) {
        return customerRepository.findAll(pageable).map(this::mapToResponse);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone());
    }
}
