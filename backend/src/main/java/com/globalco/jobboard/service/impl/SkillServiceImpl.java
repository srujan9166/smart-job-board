package com.globalco.jobboard.service.impl;

import com.globalco.jobboard.dto.request.SkillRequestDTO;
import com.globalco.jobboard.dto.response.SkillResponseDTO;
import com.globalco.jobboard.entity.Category;
import com.globalco.jobboard.entity.Skill;
import com.globalco.jobboard.exception.DuplicateResourceException;
import com.globalco.jobboard.exception.ResourceNotFoundException;
import com.globalco.jobboard.mapper.SkillMapper;
import com.globalco.jobboard.repository.CategoryRepository;
import com.globalco.jobboard.repository.SkillRepository;
import com.globalco.jobboard.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SkillService} managing skills registry.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkillServiceImpl implements SkillService {

    private static final Logger log = LoggerFactory.getLogger(SkillServiceImpl.class);

    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;
    private final SkillMapper skillMapper;

    @Override
    @Transactional
    public SkillResponseDTO createSkill(SkillRequestDTO dto) {
        log.info("Attempting to register skill: {}", dto.getName());

        if (skillRepository.existsByName(dto.getName())) {
            log.warn("Skill registration failed - name already registered: {}", dto.getName());
            throw new DuplicateResourceException("A skill with name " + dto.getName() + " already exists.");
        }

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + dto.getCategoryId()));
        }

        Skill skill = skillMapper.toEntity(dto, category);
        Skill savedSkill = skillRepository.save(skill);

        log.info("Successfully registered skill with ID: {}", savedSkill.getId());
        return skillMapper.toResponseDTO(savedSkill);
    }

    @Override
    public SkillResponseDTO getSkillById(UUID id) {
        log.debug("Retrieving skill by ID: {}", id);
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with ID: " + id));
        return skillMapper.toResponseDTO(skill);
    }

    @Override
    public List<SkillResponseDTO> getSkillsByCategory(UUID categoryId) {
        log.debug("Retrieving skills for category ID: {}", categoryId);
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with ID: " + categoryId);
        }
        return skillRepository.findByCategoryId(categoryId).stream()
                .map(skillMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SkillResponseDTO> getAllSkills() {
        log.debug("Retrieving all skills");
        return skillRepository.findAll().stream()
                .map(skillMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SkillResponseDTO updateSkill(UUID id, SkillRequestDTO dto) {
        log.info("Updating skill with ID: {}", id);

        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with ID: " + id));

        // Check name uniqueness if name is changed
        if (!skill.getName().equalsIgnoreCase(dto.getName()) && skillRepository.existsByName(dto.getName())) {
            log.warn("Skill update failed - name already registered: {}", dto.getName());
            throw new DuplicateResourceException("A skill with name " + dto.getName() + " already exists.");
        }

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + dto.getCategoryId()));
        }

        skillMapper.updateEntity(dto, skill, category);
        Skill updatedSkill = skillRepository.save(skill);

        log.info("Successfully updated skill with ID: {}", updatedSkill.getId());
        return skillMapper.toResponseDTO(updatedSkill);
    }

    @Override
    @Transactional
    public void deleteSkill(UUID id) {
        log.info("Deleting skill with ID: {}", id);
        if (!skillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Skill not found with ID: " + id);
        }
        skillRepository.deleteById(id);
        log.info("Successfully deleted skill with ID: {}", id);
    }
}
