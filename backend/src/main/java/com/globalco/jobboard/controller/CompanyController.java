package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.CompanyRequestDTO;
import com.globalco.jobboard.dto.response.CompanyResponseDTO;
import com.globalco.jobboard.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
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
 * REST controller exposing company registry and profile management APIs.
 */
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Company Profile Management", description = "APIs for company registration, search, filters, and info updates")
public class CompanyController {

    private final CompanyService companyService;

    /**
     * Gets all companies with pagination, sorting, and name/location filtering.
     *
     * @param name company name query (optional)
     * @param location company location query (optional)
     * @param pageable page settings
     * @return 200 OK containing page of matching companies
     */
    @GetMapping
    @Operation(summary = "Search and filter companies", description = "Retrieves companies matching search name and location with pagination.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Companies list successfully retrieved")
    public ResponseEntity<Page<CompanyResponseDTO>> getCompanies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            Pageable pageable) {
        Page<CompanyResponseDTO> response = companyService.getCompanies(name, location, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Registers a new company.
     *
     * @param requestDTO company payload
     * @return 201 Created containing registered company details
     */
    @PostMapping
    @Operation(summary = "Register a new company", description = "Registers a new company profile. Company name must be globally unique.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Company successfully registered")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload details provided")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Company name already exists")
    public ResponseEntity<CompanyResponseDTO> createCompany(@Valid @RequestBody CompanyRequestDTO requestDTO) {
        CompanyResponseDTO response = companyService.createCompany(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Gets a company by ID.
     *
     * @param id company identifier
     * @return 200 OK containing company details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get company by ID", description = "Retrieves company profile matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Company successfully found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company matching the ID does not exist")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable UUID id) {
        CompanyResponseDTO response = companyService.getCompanyById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a company by URL slug.
     *
     * @param slug company slug
     * @return 200 OK containing company details
     */
    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get company by URL slug", description = "Retrieves company details matching the unique URL-friendly slug.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Company successfully found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company matching the slug does not exist")
    public ResponseEntity<CompanyResponseDTO> getCompanyBySlug(@PathVariable String slug) {
        CompanyResponseDTO response = companyService.getCompanyBySlug(slug);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing company's metadata.
     *
     * @param id company identifier
     * @param requestDTO update payload
     * @return 200 OK containing updated company details
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing company", description = "Modifies company metadata properties matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Company successfully updated")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload details provided")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company matching the ID does not exist")
    public ResponseEntity<CompanyResponseDTO> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyRequestDTO requestDTO) {
        CompanyResponseDTO response = companyService.updateCompany(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a company by ID.
     *
     * @param id company identifier
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove company profile", description = "Deletes a company matching the given UUID. Cannot delete if recruiters or jobs are linked.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Company successfully deleted")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Company has active jobs or recruiters linked")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Company matching the ID does not exist")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
