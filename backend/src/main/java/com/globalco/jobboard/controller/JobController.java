package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.JobRequestDTO;
import com.globalco.jobboard.dto.response.JobResponseDTO;
import com.globalco.jobboard.entity.ExperienceLevel;
import com.globalco.jobboard.entity.JobType;
import com.globalco.jobboard.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller exposing job listings creation, searching, filtering, and management APIs.
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    /**
     * Publishes a new job opening.
     *
     * @param requestDTO job details
     * @return 201 Created containing published job details
     */
    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO requestDTO) {
        JobResponseDTO response = jobService.createJob(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Gets a job listing by ID.
     *
     * @param id job identifier
     * @return 200 OK containing job details
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable UUID id) {
        JobResponseDTO response = jobService.getJobById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets active jobs with pagination.
     *
     * @param pageable page settings
     * @return 200 OK containing page of active jobs
     */
    @GetMapping
    public ResponseEntity<Page<JobResponseDTO>> getActiveJobs(Pageable pageable) {
        Page<JobResponseDTO> response = jobService.getActiveJobs(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Searches active job listings by keyword using full-text search.
     *
     * @param keyword search term
     * @param pageable page settings
     * @return 200 OK containing matching jobs
     */
    @GetMapping("/search")
    public ResponseEntity<Page<JobResponseDTO>> searchJobsByKeyword(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<JobResponseDTO> response = jobService.searchJobsByKeyword(keyword, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Filters active jobs by company.
     *
     * @param companyId company identifier
     * @param pageable page settings
     * @return 200 OK containing matching jobs
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<JobResponseDTO>> getJobsByCompany(
            @PathVariable UUID companyId,
            Pageable pageable) {
        Page<JobResponseDTO> response = jobService.getJobsByCompany(companyId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Filters active jobs by category.
     *
     * @param categoryId category identifier
     * @param pageable page settings
     * @return 200 OK containing matching jobs
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<JobResponseDTO>> getJobsByCategory(
            @PathVariable UUID categoryId,
            Pageable pageable) {
        Page<JobResponseDTO> response = jobService.getJobsByCategory(categoryId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Filters active jobs by location. Case-insensitive query.
     *
     * @param location location query string
     * @param pageable page settings
     * @return 200 OK containing matching jobs
     */
    @GetMapping("/location")
    public ResponseEntity<Page<JobResponseDTO>> getJobsByLocation(
            @RequestParam String location,
            Pageable pageable) {
        Page<JobResponseDTO> response = jobService.getJobsByLocation(location, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Filters active jobs by employment type.
     *
     * @param jobType employment type (e.g. FULL_TIME)
     * @param pageable page settings
     * @return 200 OK containing matching jobs
     */
    @GetMapping("/job-type")
    public ResponseEntity<Page<JobResponseDTO>> getJobsByJobType(
            @RequestParam JobType jobType,
            Pageable pageable) {
        Page<JobResponseDTO> response = jobService.getJobsByJobType(jobType, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Filters active jobs by experience level.
     *
     * @param experienceLevel target experience tier (e.g. SENIOR)
     * @param pageable page settings
     * @return 200 OK containing matching jobs
     */
    @GetMapping("/experience")
    public ResponseEntity<Page<JobResponseDTO>> getJobsByExperienceLevel(
            @RequestParam ExperienceLevel experienceLevel,
            Pageable pageable) {
        Page<JobResponseDTO> response = jobService.getJobsByExperienceLevel(experienceLevel, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing job's details.
     *
     * @param id job identifier
     * @param requestDTO update details payload
     * @return 200 OK containing updated job details
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDTO> updateJob(
            @PathVariable UUID id,
            @Valid @RequestBody JobRequestDTO requestDTO) {
        JobResponseDTO response = jobService.updateJob(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a job listing.
     *
     * @param id job identifier
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable UUID id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
