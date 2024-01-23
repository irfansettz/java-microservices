package com.example.authservice.service.impl;

import com.example.authservice.exception.CommonException;
import com.example.authservice.model.dto.request.RegisterRequestDto;
import com.example.authservice.model.entity.UserEntity;
import com.example.authservice.model.helper.ApplicationConstants;
import com.example.authservice.model.helper.LoggingSession;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthImpl implements AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final LoggingSession loggingSession;

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
            throw new CommonException(ApplicationConstants.COMMON_EXCEPTION_MESSAGE);
        }
    }
}
