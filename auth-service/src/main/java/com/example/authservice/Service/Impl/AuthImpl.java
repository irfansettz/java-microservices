package com.example.authservice.Service.Impl;

import com.example.authservice.Dto.request.LoginRequestDto;
import com.example.authservice.Dto.response.LoginResponseDto;
import com.example.authservice.Exception.AuthenticationErrorException;
import com.example.authservice.Exception.BadRequestException;
import com.example.authservice.Exception.SystemErrorException;
import com.example.authservice.Dto.request.RegisterRequestDto;
import com.example.authservice.Model.Entity.UserEntity;
import com.example.authservice.Model.Helper.ApplicationConstants;
import com.example.authservice.Model.Helper.LoggingSession;
import com.example.authservice.Repository.UserRepository;
import com.example.authservice.Service.AuthService;
import com.example.authservice.Service.TokenService;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthImpl implements AuthService {
    private final Gson gson;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final LoggingSession loggingSession;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    @Transactional
    public void createUser(RegisterRequestDto body) {
        try {
            UserEntity user = modelMapper.map(body, UserEntity.class);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (Exception e){
            log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_ERROR.replace("[string]",
                    "Error create user: " + e.getMessage()));
            throw new SystemErrorException(ApplicationConstants.SYSTEM_ERROR_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public LoginResponseDto login(LoginRequestDto body) {
        try {
            log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_INFO.replace("[string]",
                    "AUTHENTICATE: " + gson.toJson(body)));
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword()));

            String token = tokenService.generateToken(authentication);
            String refreshToken = tokenService.generateRefreshToken(authentication);

            saveRefreshToken(body.getUsername(), refreshToken);
            return new LoginResponseDto(body.getUsername(), token, refreshToken);
        } catch (AuthenticationException e){
            log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_ERROR.replace("[string]",
                    "AUTHENTICATE: " + e.getMessage()));
            throw new AuthenticationErrorException(ApplicationConstants.AUTH_EXCEPTION_MESSAGE);
        }
    }

    private void saveRefreshToken(String username, String refreshToken){
        Optional<UserEntity> data = userRepository.findByUsername(username);
        if(data.isEmpty()) throw new BadRequestException(ApplicationConstants.BAD_REQUEST_EXCEPTION_MESSAGE);
        UserEntity user = data.get();
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
