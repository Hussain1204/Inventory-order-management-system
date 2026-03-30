package com.example.inventory.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String username;
    private Set<String> roles;
}
