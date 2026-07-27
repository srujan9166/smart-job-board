package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.CompanyRequestDTO;
import com.globalco.jobboard.dto.response.CompanyResponseDTO;
import com.globalco.jobboard.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class CompanyController {

    private final CompanyService companyService;

    /**
     * Registers a new company.
     *
     * @param requestDTO company payload
     * @return 201 Created containing registered company details
     */
    @PostMapping
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
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
