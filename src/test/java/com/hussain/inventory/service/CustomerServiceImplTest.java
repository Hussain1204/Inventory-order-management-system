package com.hussain.inventory.service;

import com.hussain.inventory.dto.customer.CustomerRequest;
import com.hussain.inventory.entity.Customer;
import com.hussain.inventory.exception.ResourceNotFoundException;
import com.hussain.inventory.repository.CustomerRepository;
import com.hussain.inventory.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequest request;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Rahul");
        customer.setEmail("rahul@email.com");
        customer.setPhone("9999999999");
        request = new CustomerRequest();
        request.setName("Rahul");
        request.setEmail("rahul@email.com");
        request.setPhone("9999999999");
    }

    @Test
    void createShouldReturnSavedCustomer() {
        when(customerRepository.save(org.mockito.ArgumentMatchers.any(Customer.class))).thenReturn(customer);
        assertEquals("Rahul", customerService.create(request).getName());
    }

    @Test
    void getByIdShouldThrowWhenMissing() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getById(2L));
    }

    @Test
    void getAllShouldReturnPage() {
        when(customerRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(customer)));
        assertEquals(1, customerService.getAll(PageRequest.of(0, 10)).getTotalElements());
    }
}
