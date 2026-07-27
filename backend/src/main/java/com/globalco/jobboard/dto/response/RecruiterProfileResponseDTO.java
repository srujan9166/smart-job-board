package com.globalco.jobboard.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO representing recruiter profile details for API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfileResponseDTO {

    private UUID userId;
    private UUID companyId;
    private String companyName;
    private String jobTitle;
    private Instant createdAt;
    private Instant updatedAt;
}
