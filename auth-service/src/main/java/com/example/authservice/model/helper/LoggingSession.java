package com.example.authservice.model.helper;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
public class LoggingSession {
    private final String requestId = UUID.randomUUID().toString();
}
