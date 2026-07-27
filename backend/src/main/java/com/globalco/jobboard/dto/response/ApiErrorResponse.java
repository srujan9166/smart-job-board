package com.globalco.jobboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Standardized response payload representing API error details")
public class ApiErrorResponse {

    @Builder.Default
    @Schema(description = "Timestamp when the error occurred", example = "2026-07-27T11:47:53Z")
    private Instant timestamp = Instant.now();
    
    @Schema(description = "HTTP Status code value", example = "400")
    private int status;

    @Schema(description = "HTTP Status reason error name", example = "Bad Request")
    private String error;

    @Schema(description = "Summary message detailing the error cause", example = "Validation failed. Please correct input fields errors.")
    private String message;

    @Schema(description = "Endpoint URI path of request", example = "/api/users")
    private String path;

    @Builder.Default
    @Schema(description = "Field-level list of validation failures (present only on validation failures)")
    private List<ValidationErrorDetail> validationErrors = new ArrayList<>();
}
