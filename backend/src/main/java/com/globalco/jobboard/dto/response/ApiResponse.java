package com.globalco.jobboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

/**
 * Standardized API wrapper for successful responses.
 *
 * @param <T> payload data type
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Standardized response wrapper envelope representing a successful operation response")
public class ApiResponse<T> {

    @Builder.Default
    @Schema(description = "Indicates whether operation processed successfully", example = "true")
    private boolean success = true;

    @Schema(description = "Descriptive status feedback message", example = "Request processed successfully.")
    private String message;

    @Schema(description = "Main response payload body data")
    private T data;

    @Builder.Default
    @Schema(description = "Processing end timestamp", example = "2026-07-27T11:47:53Z")
    private Instant timestamp = Instant.now();
}
