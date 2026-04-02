package com.hussain.inventory.service;

import com.hussain.inventory.dto.product.ProductRequest;
import com.hussain.inventory.dto.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);
    ProductResponse getById(Long id);
    Page<ProductResponse> getAll(Pageable pageable);
    Page<ProductResponse> getLowStockProducts(Integer threshold, Pageable pageable);
}
