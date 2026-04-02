package com.hussain.inventory.service;

import com.hussain.inventory.dto.order.OrderItemRequest;
import com.hussain.inventory.dto.order.OrderRequest;
import com.hussain.inventory.entity.*;
import com.hussain.inventory.exception.BadRequestException;
import com.hussain.inventory.exception.ResourceNotFoundException;
import com.hussain.inventory.repository.CustomerRepository;
import com.hussain.inventory.repository.OrderRepository;
import com.hussain.inventory.repository.ProductRepository;
import com.hussain.inventory.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer customer;
    private Product product;
    private OrderRequest request;

    @BeforeEach
    void setUp() {
        customer = Customer.builder().id(1L).name("Aman").email("a@b.com").phone("1234").build();
        product = Product.builder().id(10L).name("Keyboard").category("Electronics")
                .price(BigDecimal.valueOf(1500)).quantity(20).build();

        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(10L);
        item.setQuantity(2);

        request = new OrderRequest();
        request.setCustomerId(1L);
        request.setItems(List.of(item));
    }

    @Test
    void createShouldPlaceOrderAndDeductStock() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> {
            OrderEntity o = invocation.getArgument(0);
            o.setId(100L);
            return o;
        });

        var response = orderService.create(request);

        assertEquals(100L, response.getId());
        assertEquals(BigDecimal.valueOf(3000), response.getTotalPrice());
        assertEquals(18, product.getQuantity());
    }

    @Test
    void createShouldThrowWhenCustomerMissing() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.create(request));
    }

    @Test
    void createShouldThrowWhenProductMissing() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.create(request));
    }

    @Test
    void createShouldThrowWhenStockInsufficient() {
        product.setQuantity(1);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        assertThrows(BadRequestException.class, () -> orderService.create(request));
    }

    @Test
    void getByIdShouldReturnOrder() {
        when(orderRepository.findWithDetailsById(1L)).thenReturn(Optional.of(sampleOrder(OrderStatus.PLACED)));
        assertEquals(OrderStatus.PLACED, orderService.getById(1L).getStatus());
    }

    @Test
    void getByIdShouldThrowWhenMissing() {
        when(orderRepository.findWithDetailsById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getById(1L));
    }

    @Test
    void getAllShouldReturnOrders() {
        when(orderRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(sampleOrder(OrderStatus.PLACED))));
        assertEquals(1, orderService.getAll(PageRequest.of(0, 10)).getContent().size());
    }

    @Test
    void updateStatusShouldMovePlacedToShipped() {
        OrderEntity order = sampleOrder(OrderStatus.PLACED);
        when(orderRepository.findWithDetailsById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(order);

        var response = orderService.updateStatus(1L, OrderStatus.SHIPPED);
        assertEquals(OrderStatus.SHIPPED, response.getStatus());
    }

    @Test
    void updateStatusShouldThrowWhenDeliveredDirectlyFromPlaced() {
        when(orderRepository.findWithDetailsById(1L)).thenReturn(Optional.of(sampleOrder(OrderStatus.PLACED)));
        assertThrows(BadRequestException.class, () -> orderService.updateStatus(1L, OrderStatus.DELIVERED));
    }

    @Test
    void updateStatusShouldThrowWhenAlreadyDelivered() {
        when(orderRepository.findWithDetailsById(1L)).thenReturn(Optional.of(sampleOrder(OrderStatus.DELIVERED)));
        assertThrows(BadRequestException.class, () -> orderService.updateStatus(1L, OrderStatus.SHIPPED));
    }

    @Test
    void updateStatusShouldThrowWhenOrderMissing() {
        when(orderRepository.findWithDetailsById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.updateStatus(1L, OrderStatus.SHIPPED));
    }

    @Test
    void updateStatusShouldAllowShippedToDelivered() {
        OrderEntity order = sampleOrder(OrderStatus.SHIPPED);
        when(orderRepository.findWithDetailsById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(order);
        assertEquals(OrderStatus.DELIVERED, orderService.updateStatus(1L, OrderStatus.DELIVERED).getStatus());
    }

    private OrderEntity sampleOrder(OrderStatus status) {
        OrderEntity order = OrderEntity.builder()
                .id(1L)
                .customer(customer)
                .status(status)
                .totalPrice(BigDecimal.valueOf(3000))
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        OrderItem item = OrderItem.builder()
                .id(1L)
                .order(order)
                .product(product)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(1500))
                .lineTotal(BigDecimal.valueOf(3000))
                .build();

        order.getItems().add(item);
        return order;
    }
}
