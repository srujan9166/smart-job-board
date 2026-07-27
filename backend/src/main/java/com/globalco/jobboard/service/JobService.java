package com.globalco.jobboard.service;

import com.globalco.jobboard.dto.request.JobRequestDTO;
import com.globalco.jobboard.dto.response.JobResponseDTO;
import com.globalco.jobboard.entity.ExperienceLevel;
import com.globalco.jobboard.entity.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service interface defining job posting business actions.
 */
public interface JobService {

    /**
     * Publishes a new job opening.
     *
     * @param dto job details
     * @return response details of the posted job
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if company, category, or poster user does not exist
     */
    JobResponseDTO createJob(JobRequestDTO dto);

    /**
     * Retrieves a job listing by its unique ID.
     *
     * @param id job identifier
     * @return job details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    JobResponseDTO getJobById(UUID id);

    /**
     * Retrieves all active job postings with pagination.
     *
     * @param pageable page settings
     * @return page of active jobs
     */
    Page<JobResponseDTO> getActiveJobs(Pageable pageable);

    /**
     * Performs a full-text search across active job listings by keyword.
     *
     * @param keyword search keyword
     * @param pageable page settings
     * @return page of active matching jobs
     */
    Page<JobResponseDTO> searchJobsByKeyword(String keyword, Pageable pageable);

    /**
     * Finds jobs posted by a company.
     *
     * @param companyId company identifier
     * @param pageable page settings
     * @return page of company jobs
     */
    Page<JobResponseDTO> getJobsByCompany(UUID companyId, Pageable pageable);

    /**
     * Finds jobs in a category.
     *
     * @param categoryId category identifier
     * @param pageable page settings
     * @return page of category jobs
     */
    Page<JobResponseDTO> getJobsByCategory(UUID categoryId, Pageable pageable);

    /**
     * Finds jobs matching location. Case-insensitive query.
     *
     * @param location location query
     * @param pageable page settings
     * @return page of matching jobs
     */
    Page<JobResponseDTO> getJobsByLocation(String location, Pageable pageable);

    /**
     * Finds jobs of a specific type (e.g. FULL_TIME).
     *
     * @param jobType employment type
     * @param pageable page settings
     * @return page of matching jobs
     */
    Page<JobResponseDTO> getJobsByJobType(JobType jobType, Pageable pageable);

    /**
     * Finds jobs at a target experience tier (e.g. SENIOR).
     *
     * @param experienceLevel experience level
     * @param pageable page settings
     * @return page of matching jobs
     */
    Page<JobResponseDTO> getJobsByExperienceLevel(ExperienceLevel experienceLevel, Pageable pageable);

    /**
     * Updates an existing job's details.
     *
     * @param id job identifier
     * @param dto update payload
     * @return updated job details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if job, category, or company is not found
     */
    JobResponseDTO updateJob(UUID id, JobRequestDTO dto);

    /**
     * Deletes a job listing by its ID.
     *
     * @param id job identifier
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    void deleteJob(UUID id);
}
