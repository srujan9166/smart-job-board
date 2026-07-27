package com.globalco.jobboard.dto.request;

import com.globalco.jobboard.entity.ExperienceLevel;
import com.globalco.jobboard.entity.JobStatus;
import com.globalco.jobboard.entity.JobType;
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
public class JobRequestDTO {

    @NotNull(message = "Company ID is required")
    private UUID companyId;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotNull(message = "Posted by user ID is required")
    private UUID postedById;

    @NotBlank(message = "Job title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String requirements;

    private String responsibilities;

    @NotBlank(message = "Location is required")
    @Size(max = 255)
    private String location;

    @NotNull(message = "Job type is required")
    private JobType jobType;

    @NotNull(message = "Experience level is required")
    private ExperienceLevel experienceLevel;

    @PositiveOrZero(message = "Minimum salary must be greater than or equal to 0")
    private BigDecimal salaryMin;

    @PositiveOrZero(message = "Maximum salary must be greater than or equal to 0")
    private BigDecimal salaryMax;

    @NotBlank(message = "Currency is required")
    @Size(max = 3)
    @Builder.Default
    private String currency = "USD";

    @NotNull(message = "Job status is required")
    @Builder.Default
    private JobStatus status = JobStatus.DRAFT;

    private Instant expiresAt;

    @AssertTrue(message = "Maximum salary must be greater than or equal to minimum salary")
    public boolean isSalaryRangeValid() {
        if (salaryMin == null || salaryMax == null) {
            return true;
        }
        return salaryMax.compareTo(salaryMin) >= 0;
    }
}
