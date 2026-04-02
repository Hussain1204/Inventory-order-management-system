package com.example.inventory.dto.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer quantity;
    private boolean lowStock;
}
