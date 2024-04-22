package com.example.authservice.Dto.response;

public class RefreshResponseDto extends LoginResponseDto{
    public RefreshResponseDto(String username, String token, String refreshToken) {
        super(username, token, refreshToken);
    }
}
