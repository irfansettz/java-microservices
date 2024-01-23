package com.example.authservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonException extends RuntimeException{
    private String message;
}
