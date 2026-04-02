package com.hussain.inventory.repository;

import com.hussain.inventory.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    Page<OrderEntity> findAll(Pageable pageable);

    @Query("SELECT o FROM OrderEntity o WHERE o.id = :id")
    @EntityGraph(attributePaths = {"customer", "items", "items.product"})
    Optional<OrderEntity> findWithDetailsById(@Param("id") Long id);
}