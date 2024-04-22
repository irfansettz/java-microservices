package com.example.authservice.Model.Helper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@Component
public class LoggingSession {
    private final String requestId = UUID.randomUUID().toString();
    private static String logPath;

    public void setLogPath(HttpServletRequest httpRequest) {
        this.logPath = httpRequest.getServletPath() + " - " + httpRequest.getMethod();
    }

    public static String getLogPath() {
        return logPath;
    }
}
