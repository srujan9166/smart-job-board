package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.SkillRequestDTO;
import com.globalco.jobboard.dto.response.SkillResponseDTO;
import com.globalco.jobboard.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller exposing global skill registry indexing APIs.
 */
@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    /**
     * Registers a new skill.
     *
     * @param requestDTO skill payload
     * @return 201 Created containing registered skill details
     */
    @PostMapping
    public ResponseEntity<SkillResponseDTO> createSkill(@Valid @RequestBody SkillRequestDTO requestDTO) {
        SkillResponseDTO response = skillService.createSkill(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Gets all skills in the index.
     *
     * @return 200 OK containing list of all skills
     */
    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getAllSkills() {
        List<SkillResponseDTO> response = skillService.getAllSkills();
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a skill by ID.
     *
     * @param id skill identifier
     * @return 200 OK containing skill details
     */
    @GetMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> getSkillById(@PathVariable UUID id) {
        SkillResponseDTO response = skillService.getSkillById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets skills belonging to a specific category.
     *
     * @param categoryId category identifier
     * @return 200 OK containing list of skills
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SkillResponseDTO>> getSkillsByCategory(@PathVariable UUID categoryId) {
        List<SkillResponseDTO> response = skillService.getSkillsByCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing skill.
     *
     * @param id skill identifier
     * @param requestDTO update payload
     * @return 200 OK containing updated skill details
     */
    @PutMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> updateSkill(
            @PathVariable UUID id,
            @Valid @RequestBody SkillRequestDTO requestDTO) {
        SkillResponseDTO response = skillService.updateSkill(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a skill by ID.
     *
     * @param id skill identifier
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable UUID id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
