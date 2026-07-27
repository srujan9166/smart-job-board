package com.globalco.jobboard.mapper;

import com.globalco.jobboard.dto.request.SkillRequestDTO;
import com.globalco.jobboard.dto.response.SkillResponseDTO;
import com.globalco.jobboard.entity.Category;
import com.globalco.jobboard.entity.Skill;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapper component converting between {@link Skill} entity and DTO types.
 */
@Component
public class SkillMapper {

    /**
     * Converts a Skill entity to SkillResponseDTO.
     *
     * @param skill skill entity
     * @return skill response DTO
     */
    public SkillResponseDTO toResponseDTO(Skill skill) {
        if (skill == null) {
            return null;
        }

        UUID categoryId = null;
        String categoryName = null;
        if (skill.getCategory() != null) {
            categoryId = skill.getCategory().getId();
            categoryName = skill.getCategory().getName();
        }

        return SkillResponseDTO.builder()
                .id(skill.getId())
                .name(skill.getName())
                .categoryId(categoryId)
                .categoryName(categoryName)
                .createdAt(skill.getCreatedAt())
                .build();
    }

    /**
     * Converts a SkillRequestDTO to a new Skill entity.
     *
     * @param dto skill request DTO
     * @param category category association
     * @return skill entity
     */
    public Skill toEntity(SkillRequestDTO dto, Category category) {
        if (dto == null) {
            return null;
        }
        return Skill.builder()
                .name(dto.getName())
                .category(category)
                .build();
    }

    /**
     * Updates an existing Skill entity with properties from SkillRequestDTO.
     *
     * @param dto skill request DTO containing updates
     * @param skill target skill entity to modify
     * @param category updated category association
     */
    public void updateEntity(SkillRequestDTO dto, Skill skill, Category category) {
        if (dto == null || skill == null) {
            return;
        }
        skill.setName(dto.getName());
        skill.setCategory(category);
    }
}
