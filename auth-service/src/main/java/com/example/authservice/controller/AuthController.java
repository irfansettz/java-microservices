package com.example.authservice.controller;

import com.example.authservice.model.dto.request.RegisterRequestDto;
import com.example.authservice.model.dto.response.GlobalResponseDto;
import com.example.authservice.model.helper.ApplicationConstants;
import com.example.authservice.model.helper.LoggingSession;
import com.example.authservice.service.AuthService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final Gson gson;
    private final LoggingSession loggingSession;
    private final AuthService authService;
    private final PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<GlobalResponseDto> resgisterUser(@RequestBody RegisterRequestDto body,
                                                           HttpServletRequest httpRequest) throws Exception {
        loggingSession.setLogPath(httpRequest);
        log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_REQUEST
                        .replace("[string]", gson.toJson(body)));

        authService.createUser(body);

        GlobalResponseDto resp = new GlobalResponseDto("201", "Ok", null);
        ResponseEntity<GlobalResponseDto> response = new ResponseEntity<>(resp, HttpStatus.CREATED);

        log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_RESPONSE
                        .replace("[string]", gson.toJson(response)));

        return response;
    }
}
