package com.globalco.jobboard.dto.request;

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
public class CompanyRequestDTO {

    @NotBlank(message = "Company name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    @URL(message = "Website must be a valid URL")
    private String website;

    @Size(max = 500)
    @URL(message = "Logo URL must be a valid URL")
    private String logoUrl;

    private String description;

    @Size(max = 100)
    private String industry;

    private LocalDate foundedDate;

    @Size(max = 255)
    private String headquarters;

    @NotNull(message = "Creator user ID is required")
    private UUID createdById;
}
