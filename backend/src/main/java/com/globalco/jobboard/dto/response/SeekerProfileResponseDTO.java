package com.globalco.jobboard.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO representing candidate profile details for API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeekerProfileResponseDTO {

    private UUID userId;
    private String bio;
    private String resumeUrl;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private Instant createdAt;
    private Instant updatedAt;

    @Builder.Default
    private List<SeekerSkillResponseDTO> seekerSkills = new ArrayList<>();
}
