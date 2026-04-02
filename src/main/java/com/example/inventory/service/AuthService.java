package com.example.inventory.service;

import com.example.inventory.dto.auth.LoginRequest;
import com.example.inventory.dto.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
