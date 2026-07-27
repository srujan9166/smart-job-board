package com.globalco.jobboard.mapper;

import com.globalco.jobboard.dto.request.CompanyRequestDTO;
import com.globalco.jobboard.dto.response.CompanyResponseDTO;
import com.globalco.jobboard.entity.Company;
import com.globalco.jobboard.entity.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapper component converting between {@link Company} entity and DTO types.
 */
@Component
public class CompanyMapper {

    /**
     * Converts a Company entity to CompanyResponseDTO.
     *
     * @param company company entity
     * @return company response DTO
     */
    public CompanyResponseDTO toResponseDTO(Company company) {
        if (company == null) {
            return null;
        }
        
        UUID createdById = null;
        String createdByEmail = null;
        if (company.getCreatedBy() != null) {
            createdById = company.getCreatedBy().getId();
            createdByEmail = company.getCreatedBy().getEmail();
        }

        return CompanyResponseDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .slug(company.getSlug())
                .website(company.getWebsite())
                .logoUrl(company.getLogoUrl())
                .description(company.getDescription())
                .industry(company.getIndustry())
                .foundedDate(company.getFoundedDate())
                .headquarters(company.getHeadquarters())
                .createdById(createdById)
                .createdByEmail(createdByEmail)
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }

    /**
     * Converts a CompanyRequestDTO to a new Company entity, generating slug.
     *
     * @param dto company request DTO
     * @param creator company creator user
     * @return company entity
     */
    public Company toEntity(CompanyRequestDTO dto, User creator) {
        if (dto == null) {
            return null;
        }
        return Company.builder()
                .name(dto.getName())
                .slug(generateSlug(dto.getName()))
                .website(dto.getWebsite())
                .logoUrl(dto.getLogoUrl())
                .description(dto.getDescription())
                .industry(dto.getIndustry())
                .foundedDate(dto.getFoundedDate())
                .headquarters(dto.getHeadquarters())
                .createdBy(creator)
                .build();
    }

    /**
     * Updates an existing Company entity with properties from CompanyRequestDTO.
     *
     * @param dto company request DTO containing updates
     * @param company target company entity to modify
     */
    public void updateEntity(CompanyRequestDTO dto, Company company) {
        if (dto == null || company == null) {
            return;
        }
        company.setName(dto.getName());
        company.setSlug(generateSlug(dto.getName()));
        company.setWebsite(dto.getWebsite());
        company.setLogoUrl(dto.getLogoUrl());
        company.setDescription(dto.getDescription());
        company.setIndustry(dto.getIndustry());
        company.setFoundedDate(dto.getFoundedDate());
        company.setHeadquarters(dto.getHeadquarters());
    }

    /**
     * Generates a URL slug from the company name.
     */
    private String generateSlug(String name) {
        if (name == null) {
            return null;
        }
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }
}
