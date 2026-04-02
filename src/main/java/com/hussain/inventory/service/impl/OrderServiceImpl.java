package com.hussain.inventory.service.impl;

import com.hussain.inventory.dto.order.*;
import com.hussain.inventory.entity.*;
import com.hussain.inventory.exception.BadRequestException;
import com.hussain.inventory.exception.ResourceNotFoundException;
import com.hussain.inventory.repository.CustomerRepository;
import com.hussain.inventory.repository.OrderRepository;
import com.hussain.inventory.repository.ProductRepository;
import com.hussain.inventory.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @org.springframework.beans.factory.annotation.Autowired
    private OrderRepository orderRepository;
    @org.springframework.beans.factory.annotation.Autowired
    private ProductRepository productRepository;
    @org.springframework.beans.factory.annotation.Autowired
    private CustomerRepository customerRepository;

    @Override
    public OrderResponse create(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        OrderEntity order = OrderEntity.builder()
                .customer(customer)
                .status(OrderStatus.PLACED)
                .createdAt(LocalDateTime.now())
                .totalPrice(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemRequest.getProductId()));

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());

            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(lineTotal);

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(product.getPrice())
                    .lineTotal(lineTotal)
                    .build();
            orderItems.add(item);
        }

        order.setTotalPrice(total);
        order.getItems().addAll(orderItems);
        OrderEntity saved = orderRepository.save(order);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        OrderEntity order = orderRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        OrderEntity order = orderRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        validateStatusTransition(order.getStatus(), status);
        order.setStatus(status);

        return mapToResponse(orderRepository.save(order));
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == OrderStatus.DELIVERED) {
            throw new BadRequestException("Delivered order status cannot be changed");
        }
        if (current == OrderStatus.PLACED && next == OrderStatus.DELIVERED) {
            throw new BadRequestException("Order must be SHIPPED before DELIVERED");
        }
    }

    private OrderResponse mapToResponse(OrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getName())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream().map(item -> OrderItemResponse.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .lineTotal(item.getLineTotal())
                        .build()).toList())
                .build();
    }
}
