package com.hussain.inventory.config;

import com.hussain.inventory.entity.AppUser;
import com.hussain.inventory.entity.Role;
import com.hussain.inventory.entity.RoleName;
import com.hussain.inventory.repository.RoleRepository;
import com.hussain.inventory.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @org.springframework.beans.factory.annotation.Autowired
    private RoleRepository roleRepository;
    @org.springframework.beans.factory.annotation.Autowired
    private UserRepository userRepository;
    @org.springframework.beans.factory.annotation.Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleName.ROLE_ADMIN);
                    return roleRepository.save(role);
                });

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleName.ROLE_USER);
                    return roleRepository.save(role);
                });

        if (!userRepository.existsByUsername("admin")) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole, userRole));
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("user")) {
            AppUser user = new AppUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
        }
    }
}
