package com.globalco.jobboard.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

/**
 * DTO for candidate profile update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeekerProfileRequestDTO {

    private String bio;

    @Size(max = 500)
    @URL(message = "Resume link must be a valid URL")
    private String resumeUrl;

    @Size(max = 255)
    @URL(message = "GitHub profile link must be a valid URL")
    private String githubUrl;

    @Size(max = 255)
    @URL(message = "LinkedIn profile link must be a valid URL")
    private String linkedinUrl;

    @Size(max = 255)
    @URL(message = "Portfolio link must be a valid URL")
    private String portfolioUrl;
}
