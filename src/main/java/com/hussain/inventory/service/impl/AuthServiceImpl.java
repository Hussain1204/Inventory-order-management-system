package com.hussain.inventory.service.impl;

import com.hussain.inventory.dto.auth.LoginRequest;
import com.hussain.inventory.dto.auth.LoginResponse;
import com.hussain.inventory.security.JwtService;
import com.hussain.inventory.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @org.springframework.beans.factory.annotation.Autowired
    private AuthenticationManager authenticationManager;
    @org.springframework.beans.factory.annotation.Autowired
    private JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String token = jwtService.generateToken(authentication.getName(), roles);
        return LoginResponse.builder()
                .token(token)
                .username(authentication.getName())
                .roles(roles)
                .build();
    }
}
