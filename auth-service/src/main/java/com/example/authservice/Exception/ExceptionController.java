package com.example.authservice.Exception;

import com.example.authservice.Dto.response.GlobalResponseDto;
import com.example.authservice.Model.Helper.ApplicationConstants;
import com.example.authservice.Model.Helper.LoggingSession;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionController {
    private final Gson gson;
    private final LoggingSession loggingSession;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponseDto> unhandledException(Exception e){
        ResponseEntity<GlobalResponseDto> response = new ResponseEntity<>(new GlobalResponseDto<>(ApplicationConstants.UNHANDLE_EXCEPTION_CODE, ApplicationConstants.UNHANDLE_EXCEPTION_MESSAGE, null ), HttpStatus.OK);
        log.info(loggingSession.getLogPath() + " | " + "UNHANDLED EXCEPTION" + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_RESPONSE.replace("[string]", gson.toJson(response)));
        return response;
    }

    @ExceptionHandler(SystemErrorException.class)
    public ResponseEntity<GlobalResponseDto> systemErrorException(SystemErrorException e){
        ResponseEntity<GlobalResponseDto> response = new ResponseEntity<>(new GlobalResponseDto<>(ApplicationConstants.SYSTEM_EROOR_EXCEPTION_CODE, e.getMessage(), null), HttpStatus.OK);
        log.info(loggingSession.getLogPath() + " | " + "SYSTEM ERROR EXCEPTION" + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_RESPONSE.replace("[string]", gson.toJson(response)));
        return response;
    }

    @ExceptionHandler({AuthenticationErrorException.class, InvalidSessionException.class})
    public ResponseEntity<GlobalResponseDto> authException(Exception e){
        ResponseEntity<GlobalResponseDto> response = new ResponseEntity<>(new GlobalResponseDto<>(ApplicationConstants.AUTH_EXCEPTION_CODE, e.getMessage(), null), HttpStatus.OK);
        log.info(loggingSession.getLogPath() + " | " + "AUTH EXCEPTION" + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_RESPONSE.replace("[string]", gson.toJson(response)));
        return response;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GlobalResponseDto> badRequetsException(BadRequestException e){
        ResponseEntity<GlobalResponseDto> response = new ResponseEntity<>(new GlobalResponseDto<>(ApplicationConstants.BAD_REQUEST_EXCEPTION_CODE, e.getMessage(), null), HttpStatus.OK);
        log.info(loggingSession.getLogPath() + " | " + "BAD REQUEST EXCEPTION" + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_RESPONSE.replace("[string]", gson.toJson(response)));
        return response;
    }
}
