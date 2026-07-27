package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.CategoryRequestDTO;
import com.globalco.jobboard.dto.response.CategoryResponseDTO;
import com.globalco.jobboard.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller exposing job classification category APIs.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Creates a new job category classification.
     *
     * @param requestDTO category payload
     * @return 201 Created containing registered category details
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO response = categoryService.createCategory(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Gets all categories.
     *
     * @return 200 OK containing list of all categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a category by ID.
     *
     * @param id category identifier
     * @return 200 OK containing category details
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable UUID id) {
        CategoryResponseDTO response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets a category by URL slug.
     *
     * @param slug category slug
     * @return 200 OK containing category details
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponseDTO> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponseDTO response = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing category's details.
     *
     * @param id category identifier
     * @param requestDTO update payload
     * @return 200 OK containing updated category details
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO response = categoryService.updateCategory(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a category by ID.
     *
     * @param id category identifier
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
