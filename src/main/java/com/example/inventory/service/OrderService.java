package com.example.inventory.service;

import com.example.inventory.dto.order.OrderRequest;
import com.example.inventory.dto.order.OrderResponse;
import com.example.inventory.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse create(OrderRequest request);
    OrderResponse getById(Long id);
    Page<OrderResponse> getAll(Pageable pageable);
    OrderResponse updateStatus(Long id, OrderStatus status);
}
