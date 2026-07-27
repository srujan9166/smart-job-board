package com.globalco.jobboard.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * DTO for recruiter profile update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfileRequestDTO {

    @NotNull(message = "Company ID association is required")
    private UUID companyId;

    @Size(max = 100)
    private String jobTitle;
}
