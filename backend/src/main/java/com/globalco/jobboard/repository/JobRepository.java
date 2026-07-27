package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing {@link Job} entity persistence.
 * Implements entity graphs and native full-text searches for performance.
 */
@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    /**
     * Retrieves all jobs with paginated support and pre-fetches company,
     * category, and postedBy data using an EntityGraph to prevent N+1 select queries.
     *
     * @param pageable page settings
     * @return page of jobs
     */
    @Override
    @EntityGraph(attributePaths = {"company", "category", "postedBy"})
    Page<Job> findAll(Pageable pageable);

    /**
     * Finds jobs filtered by their lifecycle status.
     *
     * @param status job status
     * @param pageable page settings
     * @return page of matching jobs
     */
    @EntityGraph(attributePaths = {"company", "category", "postedBy"})
    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    /**
     * Finds jobs posted by a company and matches status.
     *
     * @param companyId company identifier
     * @param status job status
     * @param pageable page settings
     * @return page of matching jobs
     */
    @EntityGraph(attributePaths = {"company", "category", "postedBy"})
    Page<Job> findByCompanyIdAndStatus(UUID companyId, JobStatus status, Pageable pageable);

    /**
     * Finds jobs belonging to a category and matches status.
     *
     * @param categoryId category identifier
     * @param status job status
     * @param pageable page settings
     * @return page of matching jobs
     */
    @EntityGraph(attributePaths = {"company", "category", "postedBy"})
    Page<Job> findByCategoryIdAndStatus(UUID categoryId, JobStatus status, Pageable pageable);

    /**
     * Finds jobs in location matching status. Case insensitive match.
     *
     * @param location location query
     * @param status job status
     * @param pageable page settings
     * @return page of matching jobs
     */
    @EntityGraph(attributePaths = {"company", "category", "postedBy"})
    Page<Job> findByLocationContainingIgnoreCaseAndStatus(String location, JobStatus status, Pageable pageable);

    /**
     * Finds jobs by employment type and matches status.
     *
     * @param jobType employment type
     * @param status job status
     * @param pageable page settings
     * @return page of matching jobs
     */
    @EntityGraph(attributePaths = {"company", "category", "postedBy"})
    Page<Job> findByJobTypeAndStatus(JobType jobType, JobStatus status, Pageable pageable);

    /**
     * Finds jobs by target experience tier and matches status.
     *
     * @param experienceLevel experience level
     * @param status job status
     * @param pageable page settings
     * @return page of matching jobs
     */
    @EntityGraph(attributePaths = {"company", "category", "postedBy"})
    Page<Job> findByExperienceLevelAndStatus(ExperienceLevel experienceLevel, JobStatus status, Pageable pageable);

    /**
     * Native PostgreSQL Full-Text Search (FTS) query. Leveraging the GIN index
     * on the vector combination of job title and description.
     *
     * @param keyword search keyword
     * @param pageable page settings
     * @return page of active matching jobs
     */
    @Query(
        value = "SELECT * FROM jobs j WHERE j.status = 'ACTIVE' " +
                "AND to_tsvector('english', j.title || ' ' || j.description) @@ plainto_tsquery('english', :keyword)",
        countQuery = "SELECT count(*) FROM jobs j WHERE j.status = 'ACTIVE' " +
                     "AND to_tsvector('english', j.title || ' ' || j.description) @@ plainto_tsquery('english', :keyword)",
        nativeQuery = true
    )
    Page<Job> searchActiveJobsByKeywordNative(@Param("keyword") String keyword, Pageable pageable);
}
