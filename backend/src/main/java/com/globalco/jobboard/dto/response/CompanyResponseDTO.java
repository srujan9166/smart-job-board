package com.globalco.jobboard.dto.response;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO representing company details for API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponseDTO {

    private UUID id;
    private String name;
    private String slug;
    private String website;
    private String logoUrl;
    private String description;
    private String industry;
    private LocalDate foundedDate;
    private String headquarters;
    private UUID createdById;
    private String createdByEmail;
    private Instant createdAt;
    private Instant updatedAt;
}
