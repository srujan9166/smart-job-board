package com.globalco.jobboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request payload for user authentication.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payload representing user login credentials")
public class LoginRequestDTO {

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Schema(description = "User email address", example = "jane.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "SecretP@ssword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
