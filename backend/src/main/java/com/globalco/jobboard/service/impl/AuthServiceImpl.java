package com.globalco.jobboard.service.impl;

import com.globalco.jobboard.dto.request.LoginRequestDTO;
import com.globalco.jobboard.dto.request.UserRequestDTO;
import com.globalco.jobboard.dto.response.LoginResponseDTO;
import com.globalco.jobboard.dto.response.UserResponseDTO;
import com.globalco.jobboard.entity.User;
import com.globalco.jobboard.mapper.UserMapper;
import com.globalco.jobboard.security.CustomUserDetails;
import com.globalco.jobboard.security.JwtService;
import com.globalco.jobboard.service.AuthService;
import com.globalco.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for user registration and credentials authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserResponseDTO register(UserRequestDTO requestDTO) {
        return userService.createUser(requestDTO);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        String jwtToken = jwtService.generateToken(userDetails);

        return LoginResponseDTO.builder()
                .accessToken(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .user(userMapper.toResponseDTO(user))
                .build();
    }
}
