package com.globalco.jobboard.service;

import com.globalco.jobboard.dto.request.UserRequestDTO;
import com.globalco.jobboard.dto.response.UserResponseDTO;

import java.util.UUID;

/**
 * Service interface defining user business actions.
 */
public interface UserService {

    /**
     * Creates/Registers a new user in the system.
     *
     * @param dto registration payload
     * @return response details of the registered user
     * @throws com.globalco.jobboard.exception.DuplicateResourceException if email is already taken
     */
    UserResponseDTO createUser(UserRequestDTO dto);

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id user identifier
     * @return user details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    UserResponseDTO getUserById(UUID id);

    /**
     * Retrieves a user by their email address.
     *
     * @param email user email
     * @return user details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    UserResponseDTO getUserByEmail(String email);

    /**
     * Updates an existing user's information.
     *
     * @param id user identifier
     * @param dto update payload
     * @return updated user details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if user not found
     * @throws com.globalco.jobboard.exception.DuplicateResourceException if email changes to a taken email
     */
    UserResponseDTO updateUser(UUID id, UserRequestDTO dto);

    /**
     * Deletes a user from the system by their ID.
     *
     * @param id user identifier
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if user not found
     */
    void deleteUser(UUID id);
}
