package com.globalco.jobboard.mapper;

import com.globalco.jobboard.dto.request.RecruiterProfileRequestDTO;
import com.globalco.jobboard.dto.response.RecruiterProfileResponseDTO;
import com.globalco.jobboard.entity.Company;
import com.globalco.jobboard.entity.RecruiterProfile;
import com.globalco.jobboard.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper component converting between {@link RecruiterProfile} entity and DTO types.
 */
@Component
public class RecruiterProfileMapper {

    /**
     * Converts a RecruiterProfile entity to RecruiterProfileResponseDTO.
     *
     * @param profile recruiter profile entity
     * @return recruiter profile response DTO
     */
    public RecruiterProfileResponseDTO toResponseDTO(RecruiterProfile profile) {
        if (profile == null) {
            return null;
        }
        return RecruiterProfileResponseDTO.builder()
                .userId(profile.getUserId())
                .companyId(profile.getCompany() != null ? profile.getCompany().getId() : null)
                .companyName(profile.getCompany() != null ? profile.getCompany().getName() : null)
                .jobTitle(profile.getJobTitle())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    /**
     * Converts a RecruiterProfileRequestDTO to a new RecruiterProfile entity.
     *
     * @param dto recruiter profile request DTO
     * @param user user association (primary key source)
     * @param company company association
     * @return recruiter profile entity
     */
    public RecruiterProfile toEntity(RecruiterProfileRequestDTO dto, User user, Company company) {
        if (dto == null) {
            return null;
        }
        return RecruiterProfile.builder()
                .user(user)
                .userId(user.getId())
                .company(company)
                .jobTitle(dto.getJobTitle())
                .build();
    }

    /**
     * Updates an existing RecruiterProfile entity with properties from RecruiterProfileRequestDTO.
     *
     * @param dto recruiter profile request DTO containing updates
     * @param profile target recruiter profile entity to modify
     * @param company updated company association
     */
    public void updateEntity(RecruiterProfileRequestDTO dto, RecruiterProfile profile, Company company) {
        if (dto == null || profile == null) {
            return;
        }
        profile.setCompany(company);
        profile.setJobTitle(dto.getJobTitle());
    }
}
