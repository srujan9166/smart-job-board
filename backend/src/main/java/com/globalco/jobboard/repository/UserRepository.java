package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.User;
import com.globalco.jobboard.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link User} entity persistence.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Finds a user by their email address.
     *
     * @param email user email
     * @return an Optional containing the found user, or empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user already exists with the given email.
     *
     * @param email user email
     * @return true if a user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds all users with a specific system role.
     *
     * @param role user access role
     * @return list of matching users
     */
    List<User> findByRole(UserRole role);
}
