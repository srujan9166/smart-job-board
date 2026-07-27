package com.globalco.jobboard.mapper;

import com.globalco.jobboard.dto.request.JobRequestDTO;
import com.globalco.jobboard.dto.response.JobResponseDTO;
import com.globalco.jobboard.dto.response.JobSkillResponseDTO;
import com.globalco.jobboard.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper component converting between {@link Job} entity and DTO types.
 */
@Component
public class JobMapper {

    /**
     * Converts a Job entity to JobResponseDTO, flattening company, category,
     * postedBy user, and skill tags.
     *
     * @param job job entity
     * @return job response DTO
     */
    public JobResponseDTO toResponseDTO(Job job) {
        if (job == null) {
            return null;
        }

        List<JobSkillResponseDTO> skillDTOs = new ArrayList<>();
        if (job.getJobSkills() != null) {
            for (JobSkill js : job.getJobSkills()) {
                if (js.getSkill() != null) {
                    skillDTOs.add(JobSkillResponseDTO.builder()
                            .skillId(js.getSkill().getId())
                            .skillName(js.getSkill().getName())
                            .importance(js.getImportance())
                            .build());
                }
            }
        }

        return JobResponseDTO.builder()
                .id(job.getId())
                .companyId(job.getCompany() != null ? job.getCompany().getId() : null)
                .companyName(job.getCompany() != null ? job.getCompany().getName() : null)
                .categoryId(job.getCategory() != null ? job.getCategory().getId() : null)
                .categoryName(job.getCategory() != null ? job.getCategory().getName() : null)
                .postedById(job.getPostedBy() != null ? job.getPostedBy().getId() : null)
                .postedByEmail(job.getPostedBy() != null ? job.getPostedBy().getEmail() : null)
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .responsibilities(job.getResponsibilities())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .currency(job.getCurrency())
                .status(job.getStatus())
                .expiresAt(job.getExpiresAt())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .jobSkills(skillDTOs)
                .build();
    }

    /**
     * Converts a JobRequestDTO to a new Job entity.
     *
     * @param dto job request DTO
     * @param company company association
     * @param category category association
     * @param postedBy poster user association
     * @return job entity
     */
    public Job toEntity(JobRequestDTO dto, Company company, Category category, User postedBy) {
        if (dto == null) {
            return null;
        }
        return Job.builder()
                .company(company)
                .category(category)
                .postedBy(postedBy)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .requirements(dto.getRequirements())
                .responsibilities(dto.getResponsibilities())
                .location(dto.getLocation())
                .jobType(dto.getJobType())
                .experienceLevel(dto.getExperienceLevel())
                .salaryMin(dto.getSalaryMin())
                .salaryMax(dto.getSalaryMax())
                .currency(dto.getCurrency())
                .status(dto.getStatus())
                .expiresAt(dto.getExpiresAt())
                .build();
    }

    /**
     * Updates an existing Job entity with properties from JobRequestDTO.
     *
     * @param dto job request DTO containing updates
     * @param job target job entity to modify
     * @param company updated company association
     * @param category updated category association
     */
    public void updateEntity(JobRequestDTO dto, Job job, Company company, Category category) {
        if (dto == null || job == null) {
            return;
        }
        job.setCompany(company);
        job.setCategory(category);
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setRequirements(dto.getRequirements());
        job.setResponsibilities(dto.getResponsibilities());
        job.setLocation(dto.getLocation());
        job.setJobType(dto.getJobType());
        job.setExperienceLevel(dto.getExperienceLevel());
        job.setSalaryMin(dto.getSalaryMin());
        job.setSalaryMax(dto.getSalaryMax());
        job.setCurrency(dto.getCurrency());
        job.setStatus(dto.getStatus());
        job.setExpiresAt(dto.getExpiresAt());
    }
}
