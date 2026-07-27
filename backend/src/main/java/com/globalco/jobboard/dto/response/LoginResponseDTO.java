package com.globalco.jobboard.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Response payload containing stateless JWT access details upon login success.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response details containing issued JWT access token upon successful authentication")
public class LoginResponseDTO {

    @Schema(description = "Bearer JWT access token used to access secured APIs", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "Type prefix of credentials token", example = "Bearer")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "Token validity lifetime in milliseconds", example = "86400000")
    private long expiresIn;

    @Schema(description = "Authenticated user profile details")
    private UserResponseDTO user;
}
