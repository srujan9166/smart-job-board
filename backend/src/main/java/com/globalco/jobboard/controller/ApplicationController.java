package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.ApplicationRequestDTO;
import com.globalco.jobboard.dto.response.ApplicationResponseDTO;
import com.globalco.jobboard.entity.ApplicationStatus;
import com.globalco.jobboard.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Applications Tracking", description = "APIs for submitting job applications, filtering submissions, and transitioning pipeline statuses")
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * Gets all job applications with pagination, sorting, and status/seeker/job filters.
     *
     * @param status application status (optional)
     * @param seekerId seeker identifier (optional)
     * @param jobId job identifier (optional)
     * @param pageable page settings
     * @return 200 OK containing page of matching applications
     */
    @GetMapping
    @Operation(summary = "Search and filter job applications", description = "Retrieves submitted job applications matching query parameters with pagination.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications page successfully retrieved")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid sorting property field values provided")
    public ResponseEntity<Page<ApplicationResponseDTO>> getApplications(
            @Parameter(description = "Filter by pipeline stage status") @RequestParam(required = false) ApplicationStatus status,
            @Parameter(description = "Filter by candidate seeker user UUID") @RequestParam(required = false) UUID seekerId,
            @Parameter(description = "Filter by job post UUID") @RequestParam(required = false) UUID jobId,
            Pageable pageable) {
        Page<ApplicationResponseDTO> response = applicationService.getApplications(status, seekerId, jobId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Submits a candidate application for a job vacancy.
     *
     * @param requestDTO application payload
     * @return 201 Created containing submitted application details
     */
    @PostMapping
    @Operation(summary = "Submit job application", description = "Submits a candidate's resume and cover letter for a job. A seeker can apply only once per job.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Application successfully submitted")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Target job posting is draft/expired or invalid payload details")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Associated job posting or candidate seeker profile not found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Candidate has already applied to this job posting")
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
    @Operation(summary = "Get application by ID", description = "Retrieves complete details of an application matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application successfully found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Application matching the ID does not exist")
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
    @Operation(summary = "Get applications by seeker", description = "Retrieves paginated applications submitted by a specific seeker user UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications page successfully retrieved")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Seeker profile matching the ID does not exist")
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
    @Operation(summary = "Get applications by job", description = "Retrieves paginated applications submitted for a specific job UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications page successfully retrieved")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Job post matching the ID does not exist")
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
    @Operation(summary = "Get applications by job and status", description = "Retrieves paginated applications for a specific job UUID filtered by pipeline status.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Applications page successfully retrieved")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Job post matching the ID does not exist")
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
    @Operation(summary = "Update application status", description = "Modifies application status and writes an audit entry in status logs.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Application status successfully updated")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid status state transition requested")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Application or updating user UUID not found")
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
    @Operation(summary = "Withdraw job application", description = "Deletes (withdraws) job application matching the given UUID from the pipeline.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Application successfully withdrawn")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Application matching the ID does not exist")
    public ResponseEntity<Void> withdrawApplication(@PathVariable UUID id) {
        applicationService.withdrawApplication(id);
        return ResponseEntity.noContent().build();
    }
}
