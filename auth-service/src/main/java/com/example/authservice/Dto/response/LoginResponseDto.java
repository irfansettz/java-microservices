package com.example.authservice.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String username;
    private String token;
    private String refreshToken;
}
