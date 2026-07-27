package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.Application;
import com.globalco.jobboard.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Application} entity persistence.
 * Employs deep EntityGraphs to fetch related job and candidate tables in single transactions.
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID>, JpaSpecificationExecutor<Application> {

    @EntityGraph(attributePaths = {"job", "job.company", "seeker", "seeker.user"})
    Page<Application> findAll(Specification<Application> spec, Pageable pageable);

    /**
     * Retrieves an application by its ID with full pre-fetching.
     *
     * @param id application identifier
     * @return optional application
     */
    @EntityGraph(attributePaths = {"job", "job.company", "seeker", "seeker.user"})
    Optional<Application> findById(UUID id);

    /**
     * Finds applications submitted by a specific seeker.
     *
     * @param seekerId seeker identifier (user_id)
     * @param pageable page settings
     * @return page of matching applications
     */
    @EntityGraph(attributePaths = {"job", "job.company", "seeker", "seeker.user"})
    Page<Application> findBySeekerUserId(UUID seekerId, Pageable pageable);

    /**
     * Finds applications submitted to a specific job vacancy.
     *
     * @param jobId job identifier
     * @param pageable page settings
     * @return page of matching applications
     */
    @EntityGraph(attributePaths = {"job", "job.company", "seeker", "seeker.user"})
    Page<Application> findByJobId(UUID jobId, Pageable pageable);

    /**
     * Finds applications filtered globally by status.
     *
     * @param status application status
     * @param pageable page settings
     * @return page of matching applications
     */
    @EntityGraph(attributePaths = {"job", "job.company", "seeker", "seeker.user"})
    Page<Application> findByStatus(ApplicationStatus status, Pageable pageable);

    /**
     * Finds applications for a specific job and status. Useful for recruiter pipelines.
     *
     * @param jobId job identifier
     * @param status application status
     * @param pageable page settings
     * @return page of matching applications
     */
    @EntityGraph(attributePaths = {"job", "job.company", "seeker", "seeker.user"})
    Page<Application> findByJobIdAndStatus(UUID jobId, ApplicationStatus status, Pageable pageable);

    /**
     * Checks if a seeker has already submitted an application to a specific job post.
     *
     * @param jobId job identifier
     * @param seekerId seeker user identifier
     * @return true if an application exists, false otherwise
     */
    boolean existsByJobIdAndSeekerUserId(UUID jobId, UUID seekerId);
}
