package com.globalco.jobboard.mapper;

import com.globalco.jobboard.dto.request.UserRequestDTO;
import com.globalco.jobboard.dto.response.UserResponseDTO;
import com.globalco.jobboard.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper component converting between {@link User} entity and DTO types.
 */
@Component
public class UserMapper {

    /**
     * Converts a User entity to UserResponseDTO.
     *
     * @param user user entity
     * @return user response DTO
     */
    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Converts a UserRequestDTO to a new User entity.
     *
     * @param dto user request DTO
     * @return user entity
     */
    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return User.builder()
                .email(dto.getEmail())
                .passwordHash(dto.getPassword()) // Assumed to be hashed in service
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .role(dto.getRole())
                .isActive(dto.getIsActive())
                .build();
    }

    /**
     * Updates an existing User entity with properties from UserRequestDTO.
     *
     * @param dto user request DTO containing updates
     * @param entity target user entity to modify
     */
    public void updateEntity(UserRequestDTO dto, User entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setRole(dto.getRole());
        entity.setIsActive(dto.getIsActive());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPasswordHash(dto.getPassword());
        }
    }
}
