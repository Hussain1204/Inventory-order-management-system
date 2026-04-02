package com.example.inventory.repository;

import com.example.inventory.entity.AppUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
