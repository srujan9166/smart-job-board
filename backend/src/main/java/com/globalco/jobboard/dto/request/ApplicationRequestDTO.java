package com.globalco.jobboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

/**
 * DTO for candidate job application submission requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payload representing a candidate job application submission")
public class ApplicationRequestDTO {

    @NotNull(message = "Job ID is required")
    @Schema(description = "surrogate UUID of targeted job posting", example = "d9e8f7a6-b5c4-4d3e-2f1a-0e9d8c7b6a5f", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID jobId;

    @NotNull(message = "Seeker user ID is required")
    @Schema(description = "surrogate UUID of candidate seeker profile user id", example = "a2c13d8b-4b10-44be-8b22-832679f22579", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID seekerId;

    @Size(max = 500)
    @URL(message = "Resume link must be a valid URL")
    @Schema(description = "S3 URL or external link hosting candidate resume file", example = "https://s3.amazonaws.com/resumes/jane_doe.pdf")
    private String resumeUrl;

    @Schema(description = "Introductory cover letter text outlining qualifications", example = "Dear hiring manager, I am very excited about this role...")
    private String coverLetter;
}
