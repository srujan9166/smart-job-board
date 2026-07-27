package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.SkillRequestDTO;
import com.globalco.jobboard.dto.response.SkillResponseDTO;
import com.globalco.jobboard.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Skills Directory", description = "APIs for registering and searching global skills criteria (e.g. Java, AWS)")
public class SkillController {

    private final SkillService skillService;

    /**
     * Registers a new skill.
     *
     * @param requestDTO skill payload
     * @return 201 Created containing registered skill details
     */
    @PostMapping
    @Operation(summary = "Register a new skill", description = "Registers a new skill in the global qualifications directory. Skill name must be unique.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Skill successfully registered")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload details provided")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Skill name already exists")
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
    @Operation(summary = "Get all skills", description = "Retrieves all skills indexed in the system.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Skills successfully retrieved")
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
    @Operation(summary = "Get skill by ID", description = "Retrieves skill properties matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Skill successfully found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Skill matching the ID does not exist")
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
    @Operation(summary = "Get skills by category", description = "Retrieves all skills linked to a specific job category UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Skills successfully retrieved")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category matching the ID does not exist")
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
    @Operation(summary = "Update an existing skill", description = "Modifies skill details (name or parent category) matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Skill successfully updated")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload details provided")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Skill matching the ID does not exist")
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
    @Operation(summary = "Remove skill", description = "Deletes a skill matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Skill successfully deleted")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Skill matching the ID does not exist")
    public ResponseEntity<Void> deleteSkill(@PathVariable UUID id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
