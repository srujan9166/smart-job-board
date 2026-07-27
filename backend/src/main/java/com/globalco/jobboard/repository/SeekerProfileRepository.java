package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.SeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing {@link SeekerProfile} entity persistence.
 */
@Repository
public interface SeekerProfileRepository extends JpaRepository<SeekerProfile, UUID> {
}
