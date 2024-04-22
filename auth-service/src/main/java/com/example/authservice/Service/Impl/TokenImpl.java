package com.example.authservice.Service.Impl;

import com.example.authservice.Dto.response.RefreshResponseDto;
import com.example.authservice.Exception.AuthenticationErrorException;
import com.example.authservice.Exception.BadRequestException;
import com.example.authservice.Exception.InvalidSessionException;
import com.example.authservice.Exception.SystemErrorException;
import com.example.authservice.Model.Entity.UserEntity;
import com.example.authservice.Model.Entity.UserSecurityEntity;
import com.example.authservice.Model.Helper.ApplicationConstants;
import com.example.authservice.Model.Helper.LoggingSession;
import com.example.authservice.Repository.UserRepository;
import com.example.authservice.Service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenImpl implements TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final LoggingSession loggingSession;
    private final UserRepository userRepository;
    @Override
    public String generateToken(Authentication authentication) {
        return getToken(authentication, 60*10);
    }

    @Override
    public String generateRefreshToken(Authentication authentication) {
        return getToken(authentication, 3600*2);
    }

    @Override
    public UserEntity decodeToken(String token) {
        try {
            log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_INFO
                    .replace("[string]", "DECODE TOKEN : " + token));

            Jwt jwtToken = jwtDecoder.decode(token);

            String userName = jwtToken.getSubject();
            Optional<UserEntity> user = userRepository.findByUsername(userName);
            if (user.isEmpty()) throw new BadRequestException(ApplicationConstants.BAD_REQUEST_EXCEPTION_MESSAGE);
            return user.get();
        } catch (JwtException e){
            log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_ERROR
                    .replace("[string]", "ERROR DECODE TOKEN : " + e.getMessage()));
            throw new InvalidSessionException(ApplicationConstants.INVALID_SESSION_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public void isTokenValid(String username, String refreshToken) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) throw new BadRequestException(ApplicationConstants.BAD_REQUEST_EXCEPTION_MESSAGE);
        if (!refreshToken.equals(user.get().getRefreshToken())) throw new InvalidSessionException(ApplicationConstants.INVALID_SESSION_EXCEPTION_MESSAGE);
    }

    @Override
    public RefreshResponseDto refreshToken(String refreshToken) {
        UserEntity user = decodeToken(refreshToken);
        UserSecurityEntity userSecurity = new UserSecurityEntity(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userSecurity, null, userSecurity.getAuthorities());
        String newToken = generateToken(auth);
        String newRefreshToken = generateRefreshToken(auth);
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);
        return new RefreshResponseDto(user.getUsername(), newToken, user.getRefreshToken());
    }

    private String getToken(Authentication authentication, Integer expiredInSeconds){
        try {
            log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_INFO
                    .replace("[string]", authentication.getName() + " Generate Token | expired: " + expiredInSeconds.toString()));

            Instant now = Instant.now();
            String scope = authentication
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(""));
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plus(expiredInSeconds, ChronoUnit.SECONDS))
                    .subject(authentication.getName())
                    .claim("scope", scope)
                    .build();
            return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        } catch (SystemErrorException e){
            log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_ERROR
                    .replace("[string]", e.getMessage()));
            throw new SystemErrorException(ApplicationConstants.SYSTEM_ERROR_EXCEPTION_MESSAGE);
        }
    }
}
