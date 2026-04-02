package com.example.inventory.service;

import com.example.inventory.dto.customer.CustomerRequest;
import com.example.inventory.entity.Customer;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.repository.CustomerRepository;
import com.example.inventory.service.impl.CustomerServiceImpl;
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
        customer = Customer.builder().id(1L).name("Rahul").email("rahul@email.com").phone("9999999999").build();
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
