package com.globalco.jobboard.service;

import com.globalco.jobboard.dto.request.CompanyRequestDTO;
import com.globalco.jobboard.dto.response.CompanyResponseDTO;

import java.util.UUID;

/**
 * Service interface defining company business actions.
 */
public interface CompanyService {

    /**
     * Registers a new company.
     *
     * @param dto company details
     * @return response details of the registered company
     * @throws com.globalco.jobboard.exception.DuplicateResourceException if company name is already registered
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if creator user does not exist
     */
    CompanyResponseDTO createCompany(CompanyRequestDTO dto);

    /**
     * Retrieves a company by its unique ID.
     *
     * @param id company identifier
     * @return company details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    CompanyResponseDTO getCompanyById(UUID id);

    /**
     * Retrieves a company by its URL slug.
     *
     * @param slug company URL-friendly slug
     * @return company details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    CompanyResponseDTO getCompanyBySlug(String slug);

    /**
     * Updates an existing company's metadata.
     *
     * @param id company identifier
     * @param dto update payload
     * @return updated company details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if company not found
     * @throws com.globalco.jobboard.exception.DuplicateResourceException if name is changed to an already registered name
     */
    CompanyResponseDTO updateCompany(UUID id, CompanyRequestDTO dto);

    /**
     * Deletes a company by its ID.
     *
     * @param id company identifier
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     * @throws com.globalco.jobboard.exception.InvalidOperationException if company has recruiters or active jobs
     */
    void deleteCompany(UUID id);
}
