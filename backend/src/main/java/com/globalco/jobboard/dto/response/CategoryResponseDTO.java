package com.globalco.jobboard.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO representing category details for API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDTO {

    private UUID id;
    private String name;
    private String slug;
    private String description;
    private Instant createdAt;
}
