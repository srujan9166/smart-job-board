package com.globalco.jobboard.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Standardized API error response payload sent to consumers when exceptions occur.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {

    @Builder.Default
    private Instant timestamp = Instant.now();
    
    private int status;
    private String error;
    private String message;
    private String path;

    @Builder.Default
    private List<ValidationErrorDetail> validationErrors = new ArrayList<>();
}
