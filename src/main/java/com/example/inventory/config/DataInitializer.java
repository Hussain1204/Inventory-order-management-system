package com.example.inventory.config;

import com.example.inventory.entity.*;
import com.example.inventory.repository.RoleRepository;
import com.example.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_USER).build()));

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(AppUser.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(adminRole, userRole))
                    .build());
        }

        if (!userRepository.existsByUsername("user")) {
            userRepository.save(AppUser.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .roles(Set.of(userRole))
                    .build());
        }
    }
}
