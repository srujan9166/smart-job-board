package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.JobRequestDTO;
import com.globalco.jobboard.dto.response.JobResponseDTO;
import com.globalco.jobboard.entity.ExperienceLevel;
import com.globalco.jobboard.entity.JobType;
import com.globalco.jobboard.service.JobService;
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
 * REST controller exposing job listings creation, searching, filtering, and management APIs.
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Listings Operations", description = "APIs for publishing, updating, deleting, searching, and filtering job postings")
public class JobController {

    private final JobService jobService;

    /**
     * Publishes a new job opening.
     *
     * @param requestDTO job details
     * @return 201 Created containing published job details
     */
    @PostMapping
    @Operation(summary = "Publish a new job opening", description = "Creates a new job post under a company. Enforces salary validations.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Job opening successfully published")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload details or salary range bounds")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Associated company, category, or poster user not found")
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
    @Operation(summary = "Get job listing by ID", description = "Retrieves complete details of a job posting matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Job post successfully found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Job post matching the ID does not exist")
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
    @Operation(summary = "Search and filter active job postings", description = "Query, search, filter, paginate, and sort active job listings.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Jobs page successfully retrieved")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid pagination settings or sort parameter properties")
    public ResponseEntity<Page<JobResponseDTO>> getActiveJobs(
            @Parameter(description = "Keyword search matching title, description, company name, categories, location, or skills") @RequestParam(required = false) String keyword,
            @Parameter(description = "Filter jobs by specific category UUID") @RequestParam(required = false) UUID categoryId,
            @Parameter(description = "Filter jobs by specific company UUID") @RequestParam(required = false) UUID companyId,
            @Parameter(description = "Partial match case-insensitive location filter") @RequestParam(required = false) String location,
            @Parameter(description = "Filter jobs by employment type") @RequestParam(required = false) JobType jobType,
            @Parameter(description = "Filter jobs by experience seniority level") @RequestParam(required = false) ExperienceLevel experienceLevel,
            @Parameter(description = "Minimum base annual salary filter range") @RequestParam(required = false) java.math.BigDecimal salaryMin,
            @Parameter(description = "Maximum base annual salary filter range") @RequestParam(required = false) java.math.BigDecimal salaryMax,
            Pageable pageable) {
        Page<JobResponseDTO> response = jobService.searchAndFilterJobs(
                keyword, categoryId, companyId, location, jobType, experienceLevel, salaryMin, salaryMax, pageable);
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
    @Operation(summary = "Keyword FTS Search", description = "Performs keyword searches using native PostgreSQL Full-Text Search indexing.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "FTS jobs page successfully retrieved")
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
    @Operation(summary = "Filter jobs by company", description = "Retrieves active job listings posted by a specific company UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Jobs page successfully retrieved")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company matching the ID does not exist")
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
    @Operation(summary = "Filter jobs by category", description = "Retrieves active job listings classified under a category UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Jobs page successfully retrieved")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category matching the ID does not exist")
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
    @Operation(summary = "Filter jobs by location", description = "Retrieves active job listings containing the given partial location string.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Jobs page successfully retrieved")
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
    @Operation(summary = "Filter jobs by job type", description = "Retrieves active job listings matching the employment type parameter.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Jobs page successfully retrieved")
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
    @Operation(summary = "Filter jobs by experience level", description = "Retrieves active job listings matching the seniority level parameter.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Jobs page successfully retrieved")
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
    @Operation(summary = "Update job listing details", description = "Modifies properties of a job listing matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Job post successfully updated")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload details or salary bounds provided")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Job post matching the ID does not exist")
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
    @Operation(summary = "Delete job listing", description = "Deletes a job posting matching the given UUID from the directory.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Job post successfully deleted")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Job post matching the ID does not exist")
    public ResponseEntity<Void> deleteJob(@PathVariable UUID id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
