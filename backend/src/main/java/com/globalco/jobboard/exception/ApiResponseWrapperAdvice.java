package com.globalco.jobboard.exception;

import com.globalco.jobboard.dto.response.ApiErrorResponse;
import com.globalco.jobboard.dto.response.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * REST advice component that automatically intercepts successful response payloads
 * and wraps them in a uniform {@link ApiResponse} structure.
 */
@RestControllerAdvice(basePackages = "com.globalco.jobboard.controller")
public class ApiResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        
        // Prevent double wrapping if the body is already standard response models
        if (body instanceof ApiResponse || body instanceof ApiErrorResponse) {
            return body;
        }

        // Skip string responses to prevent StringHttpMessageConverter casting issues
        if (body instanceof String) {
            return body;
        }

        return ApiResponse.builder()
                .success(true)
                .message("Request processed successfully.")
                .data(body)
                .build();
    }
}
