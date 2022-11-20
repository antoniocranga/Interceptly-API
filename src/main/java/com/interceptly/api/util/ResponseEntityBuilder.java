package com.interceptly.api.util;


import com.interceptly.api.model.ApiErrorModel;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {
    public static ResponseEntity<Object> build(ApiErrorModel apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}