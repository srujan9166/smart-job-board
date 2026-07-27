package com.globalco.jobboard.dto.response;

import com.globalco.jobboard.entity.SkillImportance;
import lombok.*;

import java.util.UUID;

/**
 * DTO representing skill tags on a job posting.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSkillResponseDTO {

    private UUID skillId;
    private String skillName;
    private SkillImportance importance;
}
