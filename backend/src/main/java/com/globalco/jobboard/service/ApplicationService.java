package com.globalco.jobboard.service;

import com.globalco.jobboard.dto.request.ApplicationRequestDTO;
import com.globalco.jobboard.dto.response.ApplicationResponseDTO;
import com.globalco.jobboard.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service interface defining job application business actions.
 */
public interface ApplicationService {

    /**
     * Submits a candidate's application for a job.
     *
     * @param dto application payload
     * @return response details of the submitted application
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if job or seeker profile does not exist
     * @throws com.globalco.jobboard.exception.DuplicateResourceException if candidate has already applied to this job
     * @throws com.globalco.jobboard.exception.InvalidOperationException if the target job is not ACTIVE
     */
    ApplicationResponseDTO applyForJob(ApplicationRequestDTO dto);

    /**
     * Retrieves an application by its unique ID.
     *
     * @param id application identifier
     * @return application details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    ApplicationResponseDTO getApplicationById(UUID id);

    /**
     * Retrieves applications submitted by a job seeker.
     *
     * @param seekerId seeker identifier (user_id)
     * @param pageable page settings
     * @return page of applications
     */
    Page<ApplicationResponseDTO> getApplicationsBySeeker(UUID seekerId, Pageable pageable);

    /**
     * Retrieves applications submitted for a job post.
     *
     * @param jobId job identifier
     * @param pageable page settings
     * @return page of applications
     */
    Page<ApplicationResponseDTO> getApplicationsByJob(UUID jobId, Pageable pageable);

    /**
     * Retrieves applications for a job filtered by status. Useful for pipelines.
     *
     * @param jobId job identifier
     * @param status application status
     * @param pageable page settings
     * @return page of applications
     */
    Page<ApplicationResponseDTO> getApplicationsByJobAndStatus(UUID jobId, ApplicationStatus status, Pageable pageable);

    /**
     * Updates the status of an application and logs the transition in the history.
     *
     * @param id application identifier
     * @param status target status
     * @param actorId identifier of the user changing the status
     * @param notes status change comments
     * @return updated application details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if application or actor does not exist
     * @throws com.globalco.jobboard.exception.InvalidOperationException if transition is invalid (e.g. from closed states)
     */
    ApplicationResponseDTO updateApplicationStatus(UUID id, ApplicationStatus status, UUID actorId, String notes);

    /**
     * Withdraws an application.
     *
     * @param id application identifier
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    void withdrawApplication(UUID id);
}
