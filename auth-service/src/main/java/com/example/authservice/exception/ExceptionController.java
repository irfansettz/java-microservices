package com.example.authservice.exception;

import com.example.authservice.model.dto.response.GlobalResponseDto;
import com.example.authservice.model.helper.ApplicationConstants;
import com.example.authservice.model.helper.LoggingSession;
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

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<GlobalResponseDto> commonException(CommonException e){
        ResponseEntity<GlobalResponseDto> response = new ResponseEntity<>(new GlobalResponseDto<>(ApplicationConstants.COMMON_EXCEPTION_CODE, e.getMessage(), null ), HttpStatus.SERVICE_UNAVAILABLE);
        log.info(loggingSession.getLogPath() + " | " + loggingSession.getRequestId() + " | " + ApplicationConstants.LOG_FLAG_RESPONSE.replace("[string]", gson.toJson(response)));
        return response;
    }
}
