package com.example.inventory.service.impl;

import com.example.inventory.dto.product.ProductRequest;
import com.example.inventory.dto.product.ProductResponse;
import com.example.inventory.entity.Product;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 10;
    private final ProductRepository productRepository;

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = mapToEntity(request);
        return mapToResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getLowStockProducts(Integer threshold, Pageable pageable) {
        int finalThreshold = threshold == null ? DEFAULT_LOW_STOCK_THRESHOLD : threshold;
        return productRepository.findByQuantityLessThanEqual(finalThreshold, pageable)
                .map(this::mapToResponse);
    }

    private Product mapToEntity(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .category(request.getCategory())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .lowStock(product.getQuantity() <= DEFAULT_LOW_STOCK_THRESHOLD)
                .build();
    }
}
