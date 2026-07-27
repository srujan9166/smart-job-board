package com.globalco.jobboard.dto.response;

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
public class ApiResponse<T> {

    @Builder.Default
    private boolean success = true;

    private String message;
    private T data;

    @Builder.Default
    private Instant timestamp = Instant.now();
}
