package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.ApplicationRequestDTO;
import com.globalco.jobboard.dto.response.ApplicationResponseDTO;
import com.globalco.jobboard.entity.ApplicationStatus;
import com.globalco.jobboard.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller exposing candidate job applications and status transition APIs.
 */
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * Submits a candidate application for a job vacancy.
     *
     * @param requestDTO application payload
     * @return 201 Created containing submitted application details
     */
    @PostMapping
    public ResponseEntity<ApplicationResponseDTO> applyForJob(@Valid @RequestBody ApplicationRequestDTO requestDTO) {
        ApplicationResponseDTO response = applicationService.applyForJob(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Gets an application by ID.
     *
     * @param id application identifier
     * @return 200 OK containing application details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> getApplicationById(@PathVariable UUID id) {
        ApplicationResponseDTO response = applicationService.getApplicationById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets job applications submitted by a seeker.
     *
     * @param seekerId seeker identifier (user_id)
     * @param pageable page settings
     * @return 200 OK containing page of seeker applications
     */
    @GetMapping("/seeker/{seekerId}")
    public ResponseEntity<Page<ApplicationResponseDTO>> getApplicationsBySeeker(
            @PathVariable UUID seekerId,
            Pageable pageable) {
        Page<ApplicationResponseDTO> response = applicationService.getApplicationsBySeeker(seekerId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets job applications submitted to a job opening.
     *
     * @param jobId job identifier
     * @param pageable page settings
     * @return 200 OK containing page of job applications
     */
    @GetMapping("/job/{jobId}")
    public ResponseEntity<Page<ApplicationResponseDTO>> getApplicationsByJob(
            @PathVariable UUID jobId,
            Pageable pageable) {
        Page<ApplicationResponseDTO> response = applicationService.getApplicationsByJob(jobId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets job applications for a job opening filtered by pipeline status.
     *
     * @param jobId job identifier
     * @param status application status (e.g. SCREENING)
     * @param pageable page settings
     * @return 200 OK containing page of job applications
     */
    @GetMapping("/job/{jobId}/status")
    public ResponseEntity<Page<ApplicationResponseDTO>> getApplicationsByJobAndStatus(
            @PathVariable UUID jobId,
            @RequestParam ApplicationStatus status,
            Pageable pageable) {
        Page<ApplicationResponseDTO> response = applicationService.getApplicationsByJobAndStatus(jobId, status, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an application status and records a change log entry.
     *
     * @param id application identifier
     * @param status target status
     * @param actorId identifier of user updating the status
     * @param notes status change comments
     * @return 200 OK containing updated application details
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationResponseDTO> updateApplicationStatus(
            @PathVariable UUID id,
            @RequestParam ApplicationStatus status,
            @RequestParam UUID actorId,
            @RequestParam(required = false) String notes) {
        ApplicationResponseDTO response = applicationService.updateApplicationStatus(id, status, actorId, notes);
        return ResponseEntity.ok(response);
    }

    /**
     * Withdraws (deletes) a job application.
     *
     * @param id application identifier
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> withdrawApplication(@PathVariable UUID id) {
        applicationService.withdrawApplication(id);
        return ResponseEntity.noContent().build();
    }
}
