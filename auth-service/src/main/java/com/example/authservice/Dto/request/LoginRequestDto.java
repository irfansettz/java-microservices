package com.example.authservice.Dto.request;


import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
