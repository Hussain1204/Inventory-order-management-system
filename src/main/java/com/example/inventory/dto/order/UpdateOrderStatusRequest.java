package com.example.inventory.dto.order;

import com.example.inventory.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    @NotNull
    private OrderStatus status;
}
