package com.globalco.jobboard.dto.response;

import com.globalco.jobboard.entity.ExperienceLevel;
import com.globalco.jobboard.entity.JobStatus;
import com.globalco.jobboard.entity.JobType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO representing job details for API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response details of a job listing opening")
public class JobResponseDTO {

    @Schema(description = "Unique surrogate UUID of the job post", example = "d9e8f7a6-b5c4-4d3e-2f1a-0e9d8c7b6a5f")
    private UUID id;

    @Schema(description = "surrogate UUID of company poster", example = "ef4d52bb-7d8a-4d7a-8f5b-592f7c00e123")
    private UUID companyId;

    @Schema(description = "Name of the posting company", example = "GlobalCo Inc.")
    private String companyName;

    @Schema(description = "surrogate UUID of category classification", example = "c6c13d8b-4b10-44be-8b22-832679f22579")
    private UUID categoryId;

    @Schema(description = "Name of category", example = "Software Engineering")
    private String categoryName;

    @Schema(description = "surrogate UUID of user poster", example = "a2c13d8b-4b10-44be-8b22-832679f22579")
    private UUID postedById;

    @Schema(description = "Email of user poster", example = "jane.doe@example.com")
    private String postedByEmail;

    @Schema(description = "Job title", example = "Senior Software Engineer (Java)")
    private String title;

    @Schema(description = "Descriptive role responsibilities overview", example = "We are seeking a senior Java developer...")
    private String description;

    @Schema(description = "Newline-separated list of qualification requirements", example = "5+ years Java\nREST services experience")
    private String requirements;

    @Schema(description = "Newline-separated list of daily responsibilities", example = "Design APIs\nCode review")
    private String responsibilities;

    @Schema(description = "Location city and country, or Remote", example = "San Francisco, CA, USA")
    private String location;

    @Schema(description = "Employment terms", example = "FULL_TIME")
    private JobType jobType;

    @Schema(description = "Seniority experience target level", example = "SENIOR")
    private ExperienceLevel experienceLevel;

    @Schema(description = "Minimum base annual salary range", example = "100000.00")
    private BigDecimal salaryMin;

    @Schema(description = "Maximum base annual salary range", example = "140000.00")
    private BigDecimal salaryMax;

    @Schema(description = "ISO-4217 Currency representation", example = "USD")
    private String currency;

    @Schema(description = "Lifecycle status of job listing", example = "ACTIVE")
    private JobStatus status;

    @Schema(description = "Expiration timestamp when listing becomes inactive")
    private Instant expiresAt;

    @Schema(description = "Timestamp when job post was published")
    private Instant createdAt;

    @Schema(description = "Timestamp when job post metadata was last updated")
    private Instant updatedAt;

    @Builder.Default
    @Schema(description = "List of required/preferred skill associations with importance markers")
    private List<JobSkillResponseDTO> jobSkills = new ArrayList<>();
}
