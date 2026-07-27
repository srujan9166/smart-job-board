package com.globalco.jobboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * DTO for skill creation or update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillRequestDTO {

    @NotBlank(message = "Skill name is required")
    @Size(max = 100)
    private String name;

    private UUID categoryId;
}
