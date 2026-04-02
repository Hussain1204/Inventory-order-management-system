package com.hussain.inventory.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @NotNull
    private Long customerId;
    @NotEmpty
    private List<@Valid OrderItemRequest> items;
}
