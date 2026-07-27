package com.globalco.jobboard.service.impl;

import com.globalco.jobboard.dto.request.ApplicationRequestDTO;
import com.globalco.jobboard.dto.response.ApplicationResponseDTO;
import com.globalco.jobboard.entity.*;
import com.globalco.jobboard.exception.DuplicateResourceException;
import com.globalco.jobboard.exception.InvalidOperationException;
import com.globalco.jobboard.exception.ResourceNotFoundException;
import com.globalco.jobboard.mapper.ApplicationMapper;
import com.globalco.jobboard.repository.ApplicationRepository;
import com.globalco.jobboard.repository.JobRepository;
import com.globalco.jobboard.repository.SeekerProfileRepository;
import com.globalco.jobboard.repository.UserRepository;
import com.globalco.jobboard.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.domain.Specification;
import com.globalco.jobboard.repository.specification.ApplicationSpecification;

import java.util.UUID;

/**
 * Implementation of {@link ApplicationService} managing candidate job applications.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final SeekerProfileRepository seekerProfileRepository;
    private final UserRepository userRepository;
    private final ApplicationMapper applicationMapper;

    @Override
    @Transactional
    public ApplicationResponseDTO applyForJob(ApplicationRequestDTO dto) {
        log.info("Attempting to apply for job ID {} by seeker ID {}", dto.getJobId(), dto.getSeekerId());

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + dto.getJobId()));

        if (job.getStatus() != JobStatus.ACTIVE) {
            log.warn("Application failed - job status is not ACTIVE: {}", job.getStatus());
            throw new InvalidOperationException("Cannot submit applications to inactive job postings.");
        }

        SeekerProfile seeker = seekerProfileRepository.findById(dto.getSeekerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seeker profile not found with ID: " + dto.getSeekerId()));

        if (applicationRepository.existsByJobIdAndSeekerUserId(dto.getJobId(), dto.getSeekerId())) {
            log.warn("Application failed - seeker has already applied to this job");
            throw new DuplicateResourceException("You have already applied to this job posting.");
        }

        Application application = applicationMapper.toEntity(dto, job, seeker);
        application.setStatus(ApplicationStatus.APPLIED);

        // Seed initial audit trail record
        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .application(application)
                .status(ApplicationStatus.APPLIED)
                .changedBy(seeker.getUser())
                .notes("Initial application submission via job portal.")
                .build();
        application.getStatusHistory().add(history);

        Application savedApplication = applicationRepository.save(application);
        log.info("Successfully submitted application with ID: {}", savedApplication.getId());
        return applicationMapper.toResponseDTO(savedApplication);
    }

    @Override
    public ApplicationResponseDTO getApplicationById(UUID id) {
        log.debug("Retrieving application by ID: {}", id);
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));
        return applicationMapper.toResponseDTO(application);
    }

    @Override
    @Transactional
    public Page<ApplicationResponseDTO> getApplicationsBySeeker(UUID seekerId, Pageable pageable) {
        log.debug("Retrieving applications for seeker ID: {}", seekerId);
        if (!seekerProfileRepository.existsById(seekerId)) {
            User user = userRepository.findById(seekerId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + seekerId));
            if (user.getRole() == UserRole.JOB_SEEKER) {
                SeekerProfile newProfile = SeekerProfile.builder()
                        .user(user)
                        .build();
                seekerProfileRepository.save(newProfile);
            } else {
                throw new ResourceNotFoundException("User with ID: " + seekerId + " is not a JOB_SEEKER.");
            }
        }
        return applicationRepository.findBySeekerUserId(seekerId, pageable)
                .map(applicationMapper::toResponseDTO);
    }

    @Override
    public Page<ApplicationResponseDTO> getApplicationsByJob(UUID jobId, Pageable pageable) {
        log.debug("Retrieving applications for job ID: {}", jobId);
        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found with ID: " + jobId);
        }
        return applicationRepository.findByJobId(jobId, pageable)
                .map(applicationMapper::toResponseDTO);
    }

    @Override
    public Page<ApplicationResponseDTO> getApplicationsByJobAndStatus(UUID jobId, ApplicationStatus status, Pageable pageable) {
        log.debug("Retrieving applications for job ID {} filtered by status {}", jobId, status);
        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found with ID: " + jobId);
        }
        return applicationRepository.findByJobIdAndStatus(jobId, status, pageable)
                .map(applicationMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public ApplicationResponseDTO updateApplicationStatus(UUID id, ApplicationStatus status, UUID actorId, String notes) {
        log.info("Updating application ID {} to status {} by user ID {}", id, status, actorId);

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));

        User actor = userRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor User not found with ID: " + actorId));

        // State validation
        if (application.getStatus() == status) {
            log.debug("Application is already in status {}, skipping update", status);
            return applicationMapper.toResponseDTO(application);
        }

        application.setStatus(status);

        // Record audit logging
        ApplicationStatusHistory history = ApplicationStatusHistory.builder()
                .application(application)
                .status(status)
                .changedBy(actor)
                .notes(notes == null || notes.trim().isEmpty() ? "Status updated by administrator/employer." : notes)
                .build();
        application.getStatusHistory().add(history);

        Application updatedApplication = applicationRepository.save(application);
        log.info("Successfully updated application status for ID: {}", updatedApplication.getId());
        return applicationMapper.toResponseDTO(updatedApplication);
    }

    @Override
    @Transactional
    public void withdrawApplication(UUID id) {
        log.info("Withdrawing job application with ID: {}", id);
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));
        applicationRepository.delete(application);
        log.info("Successfully withdrew job application with ID: {}", id);
    }

    @Override
    public Page<ApplicationResponseDTO> getApplications(ApplicationStatus status, UUID seekerId, UUID jobId, Pageable pageable) {
        log.debug("Filtering applications - status: {}, seekerId: {}, jobId: {}", status, seekerId, jobId);

        validatePageable(pageable);
        validateApplicationSort(pageable.getSort());

        Specification<Application> spec = ApplicationSpecification.filterApplications(status, seekerId, jobId);
        return applicationRepository.findAll(spec, pageable)
                .map(applicationMapper::toResponseDTO);
    }

    private void validatePageable(Pageable pageable) {
        if (pageable.getPageNumber() < 0) {
            throw new com.globalco.jobboard.exception.InvalidOperationException("Page number cannot be less than zero.");
        }
        if (pageable.getPageSize() <= 0) {
            throw new com.globalco.jobboard.exception.InvalidOperationException("Page size must be greater than zero.");
        }
        if (pageable.getPageSize() > 100) {
            throw new com.globalco.jobboard.exception.InvalidOperationException("Page size cannot exceed 100.");
        }
    }

    private void validateApplicationSort(org.springframework.data.domain.Sort sort) {
        if (sort == null) return;
        java.util.Set<String> allowedFields = java.util.Set.of("appliedAt", "status");
        for (org.springframework.data.domain.Sort.Order order : sort) {
            if (!allowedFields.contains(order.getProperty())) {
                throw new com.globalco.jobboard.exception.InvalidOperationException(
                        "Invalid sorting property: '" + order.getProperty() + "'. Allowed properties are: " + allowedFields);
            }
        }
    }
}
