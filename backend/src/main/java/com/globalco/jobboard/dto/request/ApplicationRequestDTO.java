package com.globalco.jobboard.dto.request;

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
public class ApplicationRequestDTO {

    @NotNull(message = "Job ID is required")
    private UUID jobId;

    @NotNull(message = "Seeker user ID is required")
    private UUID seekerId;

    @Size(max = 500)
    @URL(message = "Resume link must be a valid URL")
    private String resumeUrl;

    private String coverLetter;
}
