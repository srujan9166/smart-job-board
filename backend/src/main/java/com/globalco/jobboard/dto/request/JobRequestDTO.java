package com.globalco.jobboard.dto.request;

import com.globalco.jobboard.entity.ExperienceLevel;
import com.globalco.jobboard.entity.JobStatus;
import com.globalco.jobboard.entity.JobType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO for job creation or update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payload representing a new job opening post request")
public class JobRequestDTO {

    /** Server-derived for authenticated employer requests; retained for admin/legacy compatibility. */
    @Schema(description = "surrogate UUID of company poster", example = "ef4d52bb-7d8a-4d7a-8f5b-592f7c00e123", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID companyId;

    @NotNull(message = "Category ID is required")
    @Schema(description = "surrogate UUID of category classification", example = "c6c13d8b-4b10-44be-8b22-832679f22579", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID categoryId;

    /** Server-derived from the authenticated user. */
    @Schema(description = "surrogate UUID of user poster", example = "a2c13d8b-4b10-44be-8b22-832679f22579", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID postedById;

    @NotBlank(message = "Job title is required")
    @Size(max = 255)
    @Schema(description = "Descriptive title of the position opening", example = "Senior Software Engineer (Java)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank(message = "Description is required")
    @Schema(description = "Detailed markdown overview of job role", example = "We are seeking a senior Java developer...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "Newline-separated list of candidate qualifications", example = "5+ years Java\nREST services experience")
    private String requirements;

    @Schema(description = "Newline-separated list of candidate daily responsibilities", example = "Design APIs\nCode review")
    private String responsibilities;

    @NotBlank(message = "Location is required")
    @Size(max = 255)
    @Schema(description = "Location city, state and country, or Remote", example = "San Francisco, CA, USA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String location;

    @NotNull(message = "Job type is required")
    @Schema(description = "Employment terms", example = "FULL_TIME", requiredMode = Schema.RequiredMode.REQUIRED)
    private JobType jobType;

    @NotNull(message = "Experience level is required")
    @Schema(description = "Target experience seniority tier", example = "SENIOR", requiredMode = Schema.RequiredMode.REQUIRED)
    private ExperienceLevel experienceLevel;

    @PositiveOrZero(message = "Minimum salary must be greater than or equal to 0")
    @Schema(description = "Minimum annual base salary bounds (e.g. 100000.00)", example = "100000.00")
    private BigDecimal salaryMin;

    @PositiveOrZero(message = "Maximum salary must be greater than or equal to 0")
    @Schema(description = "Maximum annual base salary bounds (e.g. 140000.00)", example = "140000.00")
    private BigDecimal salaryMax;

    @NotBlank(message = "Currency is required")
    @Size(max = 3)
    @Builder.Default
    @Schema(description = "ISO-4217 Currency representation", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currency = "USD";

    @NotNull(message = "Job status is required")
    @Builder.Default
    @Schema(description = "Lifecycle state of the job listing", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    private JobStatus status = JobStatus.DRAFT;

    @Schema(description = "Timestamp when the listing expires and becomes inactive", example = "2026-10-31T23:59:59Z")
    private Instant expiresAt;

    @AssertTrue(message = "Maximum salary must be greater than or equal to minimum salary")
    public boolean isSalaryRangeValid() {
        if (salaryMin == null || salaryMax == null) {
            return true;
        }
        return salaryMax.compareTo(salaryMin) >= 0;
    }
}
