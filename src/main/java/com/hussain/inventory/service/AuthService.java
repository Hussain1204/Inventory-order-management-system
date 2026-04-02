package com.hussain.inventory.service;

import com.hussain.inventory.dto.auth.LoginRequest;
import com.hussain.inventory.dto.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
