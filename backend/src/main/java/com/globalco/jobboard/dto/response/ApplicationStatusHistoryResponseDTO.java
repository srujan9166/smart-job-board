package com.globalco.jobboard.dto.response;

import com.globalco.jobboard.entity.ApplicationStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO representing an entry in the application status audit log.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusHistoryResponseDTO {

    private UUID id;
    private UUID applicationId;
    private ApplicationStatus status;
    private UUID changedById;
    private String changedByName;
    private String notes;
    private Instant changedAt;
}
