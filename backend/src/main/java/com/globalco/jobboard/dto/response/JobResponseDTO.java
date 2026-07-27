package com.globalco.jobboard.dto.response;

import com.globalco.jobboard.entity.ExperienceLevel;
import com.globalco.jobboard.entity.JobStatus;
import com.globalco.jobboard.entity.JobType;
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
public class JobResponseDTO {

    private UUID id;
    private UUID companyId;
    private String companyName;
    private UUID categoryId;
    private String categoryName;
    private UUID postedById;
    private String postedByEmail;
    private String title;
    private String description;
    private String requirements;
    private String responsibilities;
    private String location;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency;
    private JobStatus status;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant updatedAt;

    @Builder.Default
    private List<JobSkillResponseDTO> jobSkills = new ArrayList<>();
}
