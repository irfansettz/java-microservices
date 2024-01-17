package com.example.authservice.controller;

import com.example.authservice.model.dto.request.RegisterRequestDto;
import com.example.authservice.model.dto.response.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<GlobalResponseDto> resgisterUser(@RequestBody RegisterRequestDto body){


        GlobalResponseDto resp = new GlobalResponseDto(HttpStatus.CREATED.toString(), "Ok", null);
        ResponseEntity<GlobalResponseDto> response = new ResponseEntity<>(resp, HttpStatus.CREATED);
        return response;
    }
}
