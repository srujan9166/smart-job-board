package com.globalco.jobboard.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO representing skill details for API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillResponseDTO {

    private UUID id;
    private String name;
    private UUID categoryId;
    private String categoryName;
    private Instant createdAt;
}
