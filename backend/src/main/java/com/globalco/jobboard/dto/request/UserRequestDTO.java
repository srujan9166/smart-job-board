package com.globalco.jobboard.dto.request;

import com.globalco.jobboard.entity.UserRole;
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
public class UserRequestDTO {

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Size(max = 255)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    private String lastName;

    @Size(max = 20)
    private String phoneNumber;

    @NotNull(message = "Role is required")
    private UserRole role;

    @Builder.Default
    private Boolean isActive = true;
}
