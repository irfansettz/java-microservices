package com.example.authservice.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponseDto<T> {
    private String responseCode;
    private String responseMessage;
    private T responseData;
}
