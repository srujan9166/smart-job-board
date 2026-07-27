package com.globalco.jobboard.dto.request;

import com.globalco.jobboard.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO for incoming user registration or creation requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payload representing a new user registration")
public class UserRequestDTO {

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Size(max = 255)
    @Schema(description = "Primary email address of the user", example = "jane.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "Password credential (minimum 8 characters)", example = "SecretP@ssword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    @Schema(description = "User first name", example = "Jane", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    @Schema(description = "User last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Size(max = 20)
    @Schema(description = "Contact phone number (optional)", example = "+15550199")
    private String phoneNumber;

    @NotNull(message = "Role is required")
    @Schema(description = "Account system access role", example = "SEEKER", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserRole role;

    @Builder.Default
    @Schema(description = "Initial active status of the account", example = "true")
    private Boolean isActive = true;
}
