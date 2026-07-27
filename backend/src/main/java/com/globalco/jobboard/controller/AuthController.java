package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.LoginRequestDTO;
import com.globalco.jobboard.dto.request.UserRequestDTO;
import com.globalco.jobboard.dto.response.LoginResponseDTO;
import com.globalco.jobboard.dto.response.UserResponseDTO;
import com.globalco.jobboard.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing authentication operations.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Services", description = "Endpoints for user registration and credentials validation")
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new user.
     *
     * @param requestDTO registration payload
     * @return 201 Created containing registered user details
     */
    @PostMapping("/register")
    @Operation(summary = "Register user profile", description = "Creates a new user profile and encrypts password credentials.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User successfully registered")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input payload parameters")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email address is already in use")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO response = authService.register(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Authenticates user credentials.
     *
     * @param requestDTO login credentials payload
     * @return 200 OK containing JWT token details
     */
    @PostMapping("/login")
    @Operation(summary = "Verify credentials and retrieve access token", description = "Verifies email and password to return stateless access tokens.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login credentials validated successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Malformed json fields structure")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid email or password credentials provided")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        LoginResponseDTO response = authService.login(requestDTO);
        return ResponseEntity.ok(response);
    }
}
