package com.globalco.jobboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response details of a registered company profile")
public class CompanyResponseDTO {

    @Schema(description = "Unique surrogate UUID of the company", example = "ef4d52bb-7d8a-4d7a-8f5b-592f7c00e123")
    private UUID id;

    @Schema(description = "Company legal name", example = "GlobalCo Inc.")
    private String name;

    @Schema(description = "URL-friendly unique slug generated from name", example = "globalco-inc")
    private String slug;

    @Schema(description = "Company website address", example = "https://www.globalco.com")
    private String website;

    @Schema(description = "Company logo CDN URL", example = "https://cdn.globalco.com/logo.png")
    private String logoUrl;

    @Schema(description = "Detailed descriptive overview of company operations", example = "A leading global services provider.")
    private String description;

    @Schema(description = "Industry classification category", example = "Information Technology")
    private String industry;

    @Schema(description = "Founding date", example = "2015-08-15")
    private LocalDate foundedDate;

    @Schema(description = "Headquarters location details", example = "New York, USA")
    private String headquarters;

    @Schema(description = "surrogate UUID of user creator", example = "a2c13d8b-4b10-44be-8b22-832679f22579")
    private UUID createdById;

    @Schema(description = "Email of user creator profile", example = "jane.doe@example.com")
    private String createdByEmail;

    @Schema(description = "Timestamp when company record was registered")
    private Instant createdAt;

    @Schema(description = "Timestamp when company record was last updated")
    private Instant updatedAt;
}
