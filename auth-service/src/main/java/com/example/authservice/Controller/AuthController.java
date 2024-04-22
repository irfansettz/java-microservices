package com.example.authservice.Controller;

import com.example.authservice.Dto.request.LoginRequestDto;
import com.example.authservice.Dto.request.RefreshRequestDto;
import com.example.authservice.Dto.request.RegisterRequestDto;
import com.example.authservice.Dto.response.GlobalResponseDto;
import com.example.authservice.Dto.response.LoginResponseDto;
import com.example.authservice.Dto.response.RefreshResponseDto;
import com.example.authservice.Model.Entity.UserEntity;
import com.example.authservice.Model.Helper.ApplicationConstants;
import com.example.authservice.Model.Helper.LoggingSession;
import com.example.authservice.Service.AuthService;
import com.example.authservice.Service.TokenService;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.headers.Header;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final Gson gson;
    private final LoggingSession loggingSession;
    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<GlobalResponseDto> resgisterUser(@RequestBody RegisterRequestDto body,
                                                           HttpServletRequest httpRequest) {
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

    @PostMapping("/login")
    public ResponseEntity<GlobalResponseDto> login(@RequestBody LoginRequestDto body, HttpServletRequest httpRequest){
        loggingSession.setLogPath(httpRequest);
        log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_REQUEST
                .replace("[string]", gson.toJson(body)));

        LoginResponseDto login = authService.login(body);

        GlobalResponseDto resp = new GlobalResponseDto<>("200", "Ok", login);
        ResponseEntity<GlobalResponseDto> response = new ResponseEntity<>(resp, HttpStatus.OK);
        log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_RESPONSE
                .replace("[string]", gson.toJson(response)));
        return response;
    }

    @PostMapping("/refresh")
    public ResponseEntity<GlobalResponseDto> refershToken(@RequestBody RefreshRequestDto body, HttpServletRequest httpRequest){
        loggingSession.setLogPath(httpRequest);
        log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_REQUEST
                .replace("[string]", gson.toJson(body)));

        tokenService.isTokenValid(body.getUsername(), body.getRefreshToken());
        RefreshResponseDto refresh = tokenService.refreshToken(body.getRefreshToken());

        GlobalResponseDto resp = new GlobalResponseDto<>("200", "Ok", refresh);
        ResponseEntity<GlobalResponseDto> response = new ResponseEntity<>(resp, HttpStatus.OK);
        log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_RESPONSE
                .replace("[string]", gson.toJson(response)));
        return response;
    }

    @GetMapping("/test")
    public String TestEndpoint(@RequestHeader(name = "Authorization") String tokenBearer, HttpServletRequest httpRequest){
        loggingSession.setLogPath(httpRequest);
        log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_REQUEST
                .replace("[string]", gson.toJson(null)));
        UserEntity user = tokenService.decodeToken(tokenBearer);
        return "Token Valid, Your Name Is: " + gson.toJson(user.getUsername());
    }
}
