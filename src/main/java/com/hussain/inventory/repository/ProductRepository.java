package com.hussain.inventory.repository;

import com.hussain.inventory.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByQuantityLessThanEqual(Integer quantity, Pageable pageable);
}
