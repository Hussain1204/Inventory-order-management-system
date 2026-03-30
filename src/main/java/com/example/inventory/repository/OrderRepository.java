package com.example.inventory.repository;

import com.example.inventory.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    Page<OrderEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    Optional<OrderEntity> findWithDetailsById(Long id);
}
