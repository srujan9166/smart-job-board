package com.globalco.jobboard.service;

import com.globalco.jobboard.dto.request.LoginRequestDTO;
import com.globalco.jobboard.dto.request.UserRequestDTO;
import com.globalco.jobboard.dto.response.LoginResponseDTO;
import com.globalco.jobboard.dto.response.UserResponseDTO;

/**
 * Service managing user authentication pipeline (registration and login token generation).
 */
public interface AuthService {

    /**
     * Registers a new user with BCrypt credentials encoding.
     *
     * @param requestDTO user credentials request payload
     * @return registered user profile details
     */
    UserResponseDTO register(UserRequestDTO requestDTO);

    /**
     * Authenticates login credentials and returns a valid stateless JWT.
     *
     * @param requestDTO login request credentials
     * @return login response details containing JWT
     */
    LoginResponseDTO login(LoginRequestDTO requestDTO);
}
