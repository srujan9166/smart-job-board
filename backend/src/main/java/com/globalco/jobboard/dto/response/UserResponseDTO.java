package com.globalco.jobboard.dto.response;

import com.globalco.jobboard.entity.UserRole;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * DTO representing user details for API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRole role;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}
