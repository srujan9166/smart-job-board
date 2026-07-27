package com.globalco.jobboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for company creation or update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payload representing a company registration request")
public class CompanyRequestDTO {

    @NotBlank(message = "Company name is required")
    @Size(max = 255)
    @Schema(description = "Legal registered name of the company", example = "GlobalCo Inc.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 255)
    @URL(message = "Website must be a valid URL")
    @Schema(description = "Public web address URL of the company", example = "https://www.globalco.com")
    private String website;

    @Size(max = 500)
    @URL(message = "Logo URL must be a valid URL")
    @Schema(description = "Logo image address URL hosted in a CDN", example = "https://cdn.globalco.com/logo.png")
    private String logoUrl;

    @Schema(description = "Detailed descriptive introduction of company operations and size", example = "A leading global services provider.")
    private String description;

    @Size(max = 100)
    @Schema(description = "Primary industry category of operation", example = "Information Technology")
    private String industry;

    @Schema(description = "Date when the company was founded", example = "2015-08-15")
    private LocalDate foundedDate;

    @Size(max = 255)
    @Schema(description = "Location city and country of company headquarters", example = "New York, USA")
    private String headquarters;

    @NotNull(message = "Creator user ID is required")
    @Schema(description = "surrogate UUID of user creator", example = "a2c13d8b-4b10-44be-8b22-832679f22579", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID createdById;
}
