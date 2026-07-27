package com.globalco.jobboard.dto.response;

import com.globalco.jobboard.entity.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response details representing a submitted candidate job application")
public class ApplicationResponseDTO {

    @Schema(description = "Unique surrogate UUID of the application record", example = "fc4d22cc-7d8a-4d7a-8f5b-592f7c00e567")
    private UUID id;

    @Schema(description = "surrogate UUID of target job posting", example = "d9e8f7a6-b5c4-4d3e-2f1a-0e9d8c7b6a5f")
    private UUID jobId;

    @Schema(description = "Title of target job position", example = "Senior Software Engineer (Java)")
    private String jobTitle;

    @Schema(description = "Name of target company", example = "GlobalCo Inc.")
    private String companyName;

    @Schema(description = "surrogate UUID of candidate seeker profile user id", example = "a2c13d8b-4b10-44be-8b22-832679f22579")
    private UUID seekerId;

    @Schema(description = "Candidate first name", example = "Jane")
    private String seekerFirstName;

    @Schema(description = "Candidate last name", example = "Doe")
    private String seekerLastName;

    @Schema(description = "Candidate resume external CDN link URL", example = "https://s3.amazonaws.com/resumes/jane_doe.pdf")
    private String resumeUrl;

    @Schema(description = "Submitted candidate cover letter statement", example = "Dear hiring manager, I am very excited about this role...")
    private String coverLetter;

    @Schema(description = "Current pipeline status state of application", example = "SCREENING")
    private ApplicationStatus status;

    @Schema(description = "Timestamp when application was submitted")
    private Instant appliedAt;

    @Schema(description = "Timestamp when application metadata was last updated")
    private Instant updatedAt;

    @Builder.Default
    @Schema(description = "Chronological audit change history status logs list")
    private List<ApplicationStatusHistoryResponseDTO> statusHistory = new ArrayList<>();
}
