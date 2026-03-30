package com.example.inventory.controller;

import com.example.inventory.dto.auth.LoginRequest;
import com.example.inventory.dto.auth.LoginResponse;
import com.example.inventory.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> currentUser(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "roles", authentication.getAuthorities()));
    }
}
