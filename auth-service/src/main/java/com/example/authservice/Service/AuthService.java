package com.example.authservice.Service;

import com.example.authservice.Dto.request.LoginRequestDto;
import com.example.authservice.Dto.request.RegisterRequestDto;
import com.example.authservice.Dto.response.LoginResponseDto;

public interface AuthService {
    void createUser(RegisterRequestDto body);

    LoginResponseDto login(LoginRequestDto body);
}
