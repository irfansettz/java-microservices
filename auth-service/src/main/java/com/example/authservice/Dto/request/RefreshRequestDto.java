package com.example.authservice.Dto.request;

import lombok.Data;

@Data
public class RefreshRequestDto {
    private String username;
    private String refreshToken;
}
