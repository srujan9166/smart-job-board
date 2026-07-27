package com.globalco.jobboard.dto.response;

import com.globalco.jobboard.entity.ProficiencyLevel;
import lombok.*;

import java.util.UUID;

/**
 * DTO representing skill tags on a candidate profile.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeekerSkillResponseDTO {

    private UUID skillId;
    private String skillName;
    private ProficiencyLevel proficiencyLevel;
}
