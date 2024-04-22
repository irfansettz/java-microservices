package com.example.authservice.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidSessionException extends RuntimeException {
    private String message;
}
