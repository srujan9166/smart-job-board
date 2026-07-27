package com.globalco.jobboard.service.impl;

import com.globalco.jobboard.dto.request.CompanyRequestDTO;
import com.globalco.jobboard.dto.response.CompanyResponseDTO;
import com.globalco.jobboard.entity.Company;
import com.globalco.jobboard.entity.User;
import com.globalco.jobboard.entity.UserRole;
import com.globalco.jobboard.exception.DuplicateResourceException;
import com.globalco.jobboard.exception.ResourceNotFoundException;
import com.globalco.jobboard.mapper.CompanyMapper;
import com.globalco.jobboard.repository.CompanyRepository;
import com.globalco.jobboard.repository.UserRepository;
import com.globalco.jobboard.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.globalco.jobboard.repository.specification.CompanySpecification;

import java.util.UUID;

/**
 * Implementation of {@link CompanyService} managing company profiles.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;

    @Override
    @Transactional
    public CompanyResponseDTO createCompany(CompanyRequestDTO dto) {
        log.info("Attempting to create company with name: {}", dto.getName());

        if (companyRepository.existsByName(dto.getName())) {
            log.warn("Company creation failed - name already registered: {}", dto.getName());
            throw new DuplicateResourceException("A company with name " + dto.getName() + " already exists.");
        }

        User creator = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("Creator User not found with ID: " + dto.getCreatedById()));

        Company company = companyMapper.toEntity(dto, creator);
        Company savedCompany = companyRepository.save(company);

        log.info("Successfully created company with ID: {}", savedCompany.getId());
        return companyMapper.toResponseDTO(savedCompany);
    }

    @Override
    @Transactional
    public CompanyResponseDTO createCompanyForEmployer(CompanyRequestDTO dto, UUID employerId) {
        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer not found."));
        if (employer.getRole() != UserRole.EMPLOYER) {
            throw new com.globalco.jobboard.exception.InvalidOperationException("Only employers can create a company profile.");
        }
        if (companyRepository.findFirstByCreatedByIdOrderByCreatedAtAsc(employerId).isPresent()) {
            throw new DuplicateResourceException("You already have a company profile.");
        }
        if (companyRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("A company with name " + dto.getName() + " already exists.");
        }
        return companyMapper.toResponseDTO(companyRepository.save(companyMapper.toEntity(dto, employer)));
    }

    @Override
    public CompanyResponseDTO getMyCompany(UUID employerId) {
        return companyMapper.toResponseDTO(companyRepository.findFirstByCreatedByIdOrderByCreatedAtAsc(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Company profile not found. Please configure your company profile.")));
    }

    @Override
    @Transactional
    public CompanyResponseDTO updateMyCompany(CompanyRequestDTO dto, UUID employerId) {
        Company company = companyRepository.findFirstByCreatedByIdOrderByCreatedAtAsc(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Company profile not found. Please configure your company profile."));
        if (!company.getName().equalsIgnoreCase(dto.getName()) && companyRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("A company with name " + dto.getName() + " already exists.");
        }
        companyMapper.updateEntity(dto, company);
        return companyMapper.toResponseDTO(companyRepository.save(company));
    }

    @Override
    public CompanyResponseDTO getCompanyById(UUID id) {
        log.debug("Retrieving company by ID: {}", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
        return companyMapper.toResponseDTO(company);
    }

    @Override
    public CompanyResponseDTO getCompanyBySlug(String slug) {
        log.debug("Retrieving company by slug: {}", slug);
        Company company = companyRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with slug: " + slug));
        return companyMapper.toResponseDTO(company);
    }

    @Override
    @Transactional
    public CompanyResponseDTO updateCompany(UUID id, CompanyRequestDTO dto) {
        log.info("Updating company with ID: {}", id);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));

        // Check company name uniqueness if name is changed
        if (!company.getName().equalsIgnoreCase(dto.getName()) && companyRepository.existsByName(dto.getName())) {
            log.warn("Company update failed - name already registered: {}", dto.getName());
            throw new DuplicateResourceException("A company with name " + dto.getName() + " already exists.");
        }

        companyMapper.updateEntity(dto, company);
        Company updatedCompany = companyRepository.save(company);

        log.info("Successfully updated company with ID: {}", updatedCompany.getId());
        return companyMapper.toResponseDTO(updatedCompany);
    }

    @Override
    @Transactional
    public void deleteCompany(UUID id) {
        log.info("Deleting company with ID: {}", id);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));

        // Business rule constraint: cannot delete company if it has recruiters or jobs
        if (company.getRecruiterProfiles() != null && !company.getRecruiterProfiles().isEmpty()) {
            throw new com.globalco.jobboard.exception.InvalidOperationException(
                    "Cannot delete company because recruiters are linked to it.");
        }
        if (company.getJobs() != null && !company.getJobs().isEmpty()) {
            throw new com.globalco.jobboard.exception.InvalidOperationException(
                    "Cannot delete company because jobs are linked to it.");
        }

        companyRepository.delete(company);
        log.info("Successfully deleted company with ID: {}", id);
    }

    @Override
    public Page<CompanyResponseDTO> getCompanies(String name, String location, Pageable pageable) {
        log.debug("Filtering companies by name: '{}' and location: '{}'", name, location);

        validatePageable(pageable);
        validateCompanySort(pageable.getSort());

        Specification<Company> spec = CompanySpecification.filterCompanies(name, location);
        return companyRepository.findAll(spec, pageable)
                .map(companyMapper::toResponseDTO);
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

    private void validateCompanySort(org.springframework.data.domain.Sort sort) {
        if (sort == null) return;
        java.util.Set<String> allowedFields = java.util.Set.of("name");
        for (org.springframework.data.domain.Sort.Order order : sort) {
            if (!allowedFields.contains(order.getProperty())) {
                throw new com.globalco.jobboard.exception.InvalidOperationException(
                        "Invalid sorting property: '" + order.getProperty() + "'. Allowed properties are: " + allowedFields);
            }
        }
    }
}
