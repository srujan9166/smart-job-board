package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link RecruiterProfile} entity persistence.
 */
@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, UUID> {

    /**
     * Finds recruiter profiles belonging to a specific company.
     *
     * @param companyId company identifier
     * @return list of recruiters at that company
     */
    List<RecruiterProfile> findByCompanyId(UUID companyId);
}
