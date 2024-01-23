package com.example.authservice.service;

import com.example.authservice.model.dto.request.RegisterRequestDto;

public interface AuthService {
    void createUser(RegisterRequestDto body);
}
