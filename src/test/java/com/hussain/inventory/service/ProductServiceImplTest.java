package com.hussain.inventory.service;

import com.hussain.inventory.dto.product.ProductRequest;
import com.hussain.inventory.entity.Product;
import com.hussain.inventory.exception.ResourceNotFoundException;
import com.hussain.inventory.repository.ProductRepository;
import com.hussain.inventory.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest request;

    @BeforeEach
    void setUp() {
        product = Product.builder().id(1L).name("Laptop").category("Electronics")
                .price(BigDecimal.valueOf(50000)).quantity(25).build();
        request = new ProductRequest();
        request.setName("Laptop");
        request.setCategory("Electronics");
        request.setPrice(BigDecimal.valueOf(50000));
        request.setQuantity(25);
    }

    @Test
    void createShouldPersistProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);
        assertEquals("Laptop", productService.create(request).getName());
    }

    @Test
    void updateShouldModifyExistingProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        assertEquals("Electronics", productService.update(1L, request).getCategory());
    }

    @Test
    void updateShouldThrowWhenProductMissing() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.update(1L, request));
    }

    @Test
    void deleteShouldCallRepositoryDelete() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.delete(1L);
        verify(productRepository).delete(product);
    }

    @Test
    void deleteShouldThrowWhenMissing() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.delete(1L));
    }

    @Test
    void getByIdShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertEquals(1L, productService.getById(1L).getId());
    }

    @Test
    void getByIdShouldThrowWhenNotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getById(2L));
    }

    @Test
    void getAllShouldReturnPaginatedResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.findAll(pageable)).thenReturn(page);
        assertEquals(1, productService.getAll(pageable).getContent().size());
    }

    @Test
    void getLowStockShouldUseDefaultThreshold() {
        Pageable pageable = PageRequest.of(0, 10);
        Product lowStock = Product.builder().id(2L).name("Mouse").category("Electronics")
                .price(BigDecimal.valueOf(500)).quantity(5).build();
        when(productRepository.findByQuantityLessThanEqual(10, pageable)).thenReturn(new PageImpl<>(List.of(lowStock)));
        assertTrue(productService.getLowStockProducts(null, pageable).getContent().get(0).isLowStock());
    }
}
