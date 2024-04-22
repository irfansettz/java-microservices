package com.example.authservice.Service;

import com.example.authservice.Dto.response.RefreshResponseDto;
import com.example.authservice.Model.Entity.UserEntity;
import org.springframework.security.core.Authentication;

public interface TokenService {
    String generateToken(Authentication authentication);
    String generateRefreshToken(Authentication authentication);
    UserEntity decodeToken(String tokenBearer);
    void isTokenValid(String username, String refreshToken);
    RefreshResponseDto refreshToken(String refreshToken);
}
