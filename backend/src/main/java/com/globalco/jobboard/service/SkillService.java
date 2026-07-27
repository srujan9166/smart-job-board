package com.globalco.jobboard.service;

import com.globalco.jobboard.dto.request.SkillRequestDTO;
import com.globalco.jobboard.dto.response.SkillResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining skill registry business actions.
 */
public interface SkillService {

    /**
     * Registers a new skill in the global index.
     *
     * @param dto skill details
     * @return response details of the registered skill
     * @throws com.globalco.jobboard.exception.DuplicateResourceException if skill name already exists
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if categoryId is supplied but does not exist
     */
    SkillResponseDTO createSkill(SkillRequestDTO dto);

    /**
     * Retrieves a skill by its unique ID.
     *
     * @param id skill identifier
     * @return skill details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    SkillResponseDTO getSkillById(UUID id);

    /**
     * Retrieves all skills belonging to a specific category.
     *
     * @param categoryId category identifier
     * @return list of matching skills
     */
    List<SkillResponseDTO> getSkillsByCategory(UUID categoryId);

    /**
     * Retrieves all skills registered in the system.
     *
     * @return list of all skills
     */
    List<SkillResponseDTO> getAllSkills();

    /**
     * Updates an existing skill's name or category.
     *
     * @param id skill identifier
     * @param dto update payload
     * @return updated skill details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if skill or updated category is not found
     * @throws com.globalco.jobboard.exception.DuplicateResourceException if name is changed to an already registered name
     */
    SkillResponseDTO updateSkill(UUID id, SkillRequestDTO dto);

    /**
     * Deletes a skill by its ID.
     *
     * @param id skill identifier
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    void deleteSkill(UUID id);
}
