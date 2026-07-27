package com.globalco.jobboard.mapper;

import com.globalco.jobboard.dto.request.SeekerProfileRequestDTO;
import com.globalco.jobboard.dto.response.SeekerProfileResponseDTO;
import com.globalco.jobboard.dto.response.SeekerSkillResponseDTO;
import com.globalco.jobboard.entity.SeekerProfile;
import com.globalco.jobboard.entity.SeekerSkill;
import com.globalco.jobboard.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper component converting between {@link SeekerProfile} entity and DTO types.
 */
@Component
public class SeekerProfileMapper {

    /**
     * Converts a SeekerProfile entity to SeekerProfileResponseDTO, including skill tags.
     *
     * @param profile seeker profile entity
     * @return seeker profile response DTO
     */
    public SeekerProfileResponseDTO toResponseDTO(SeekerProfile profile) {
        if (profile == null) {
            return null;
        }

        List<SeekerSkillResponseDTO> skillDTOs = new ArrayList<>();
        if (profile.getSeekerSkills() != null) {
            for (SeekerSkill ss : profile.getSeekerSkills()) {
                if (ss.getSkill() != null) {
                    skillDTOs.add(SeekerSkillResponseDTO.builder()
                            .skillId(ss.getSkill().getId())
                            .skillName(ss.getSkill().getName())
                            .proficiencyLevel(ss.getProficiencyLevel())
                            .build());
                }
            }
        }

        return SeekerProfileResponseDTO.builder()
                .userId(profile.getUserId())
                .bio(profile.getBio())
                .resumeUrl(profile.getResumeUrl())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .seekerSkills(skillDTOs)
                .build();
    }

    /**
     * Converts a SeekerProfileRequestDTO to a new SeekerProfile entity.
     *
     * @param dto seeker profile request DTO
     * @param user user association (primary key source)
     * @return seeker profile entity
     */
    public SeekerProfile toEntity(SeekerProfileRequestDTO dto, User user) {
        if (dto == null) {
            return null;
        }
        return SeekerProfile.builder()
                .user(user)
                .userId(user.getId())
                .bio(dto.getBio())
                .resumeUrl(dto.getResumeUrl())
                .githubUrl(dto.getGithubUrl())
                .linkedinUrl(dto.getLinkedinUrl())
                .portfolioUrl(dto.getPortfolioUrl())
                .build();
    }

    /**
     * Updates an existing SeekerProfile entity with properties from SeekerProfileRequestDTO.
     *
     * @param dto seeker profile request DTO containing updates
     * @param profile target profile entity to modify
     */
    public void updateEntity(SeekerProfileRequestDTO dto, SeekerProfile profile) {
        if (dto == null || profile == null) {
            return;
        }
        profile.setBio(dto.getBio());
        profile.setResumeUrl(dto.getResumeUrl());
        profile.setGithubUrl(dto.getGithubUrl());
        profile.setLinkedinUrl(dto.getLinkedinUrl());
        profile.setPortfolioUrl(dto.getPortfolioUrl());
    }
}
