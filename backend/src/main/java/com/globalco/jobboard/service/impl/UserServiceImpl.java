package com.globalco.jobboard.service.impl;

import com.globalco.jobboard.dto.request.UserRequestDTO;
import com.globalco.jobboard.dto.response.UserResponseDTO;
import com.globalco.jobboard.entity.User;
import com.globalco.jobboard.exception.DuplicateResourceException;
import com.globalco.jobboard.exception.ResourceNotFoundException;
import com.globalco.jobboard.mapper.UserMapper;
import com.globalco.jobboard.repository.UserRepository;
import com.globalco.jobboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of {@link UserService} managing user accounts.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {
        log.info("Attempting to register user with email: {}", dto.getEmail());
        
        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("Registration failed - email already exists: {}", dto.getEmail());
            throw new DuplicateResourceException("A user with email " + dto.getEmail() + " already exists.");
        }

        User user = userMapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        if (user.getRole() == com.globalco.jobboard.entity.UserRole.JOB_SEEKER) {
            com.globalco.jobboard.entity.SeekerProfile seekerProfile = com.globalco.jobboard.entity.SeekerProfile.builder()
                    .user(user)
                    .build();
            user.setSeekerProfile(seekerProfile);
        }
        
        User savedUser = userRepository.save(user);
        
        log.info("Successfully registered user with ID: {}", savedUser.getId());
        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        log.debug("Retrieving user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        log.debug("Retrieving user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {
        log.info("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        // Check email uniqueness if email is changed
        if (!user.getEmail().equalsIgnoreCase(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            log.warn("Update failed - email already exists: {}", dto.getEmail());
            throw new DuplicateResourceException("A user with email " + dto.getEmail() + " already exists.");
        }

        userMapper.updateEntity(dto, user);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        
        log.info("Successfully updated user with ID: {}", updatedUser.getId());
        return userMapper.toResponseDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        log.info("Successfully deleted user with ID: {}", id);
    }
}
