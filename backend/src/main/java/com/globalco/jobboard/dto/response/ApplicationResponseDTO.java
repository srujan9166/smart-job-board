package com.globalco.jobboard.dto.response;

import com.globalco.jobboard.entity.ApplicationStatus;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO representing job application details for API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationResponseDTO {

    private UUID id;
    private UUID jobId;
    private String jobTitle;
    private String companyName;
    private UUID seekerId;
    private String seekerFirstName;
    private String seekerLastName;
    private String resumeUrl;
    private String coverLetter;
    private ApplicationStatus status;
    private Instant appliedAt;
    private Instant updatedAt;

    @Builder.Default
    private List<ApplicationStatusHistoryResponseDTO> statusHistory = new ArrayList<>();
}
