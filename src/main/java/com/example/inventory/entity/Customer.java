package com.example.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers", indexes = @Index(name = "idx_customer_email", columnList = "email", unique = true))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;
}
