package com.globalco.jobboard.dto.response;

import lombok.*;

/**
 * Details of a single field bean validation failure.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationErrorDetail {
    private String field;
    private Object rejectedValue;
    private String message;
}
