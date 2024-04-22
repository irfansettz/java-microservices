package com.example.authservice.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SystemErrorException extends RuntimeException{
    private String message;
}
