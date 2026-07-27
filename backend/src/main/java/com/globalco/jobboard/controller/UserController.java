package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.UserRequestDTO;
import com.globalco.jobboard.dto.response.UserResponseDTO;
import com.globalco.jobboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller exposing user registration and account management APIs.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for user registration, updates, and profile inquiries")
public class UserController {

    private final UserService userService;

    /**
     * Registers a new user.
     *
     * @param requestDTO registration payload
     * @return 201 Created containing registered user details
     */
    @PostMapping
    @Operation(summary = "Register a new user", description = "Registers a new user account with role SEEKER, EMPLOYER, or ADMIN.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User successfully registered")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload details provided")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "A user with the given email address already exists")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO response = userService.createUser(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Gets a user by ID.
     *
     * @param id user identifier
     * @return 200 OK containing user details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves user account details matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User successfully found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User matching the ID does not exist")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        UserResponseDTO response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a user by email query.
     *
     * @param email user email address
     * @return 200 OK containing user details
     */
    @GetMapping("/email")
    @Operation(summary = "Get user by email", description = "Retrieves user account details matching the given email parameter.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User successfully found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User matching the email does not exist")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@RequestParam String email) {
        UserResponseDTO response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing user.
     *
     * @param id user identifier
     * @param requestDTO update payload
     * @return 200 OK containing updated user details
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Modifies user account properties matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User successfully updated")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload details provided")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User matching the ID does not exist")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO response = userService.updateUser(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id user identifier
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove user account", description = "Deletes a user account matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "User successfully deleted")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User matching the ID does not exist")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
