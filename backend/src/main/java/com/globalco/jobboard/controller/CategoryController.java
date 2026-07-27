package com.globalco.jobboard.controller;

import com.globalco.jobboard.dto.request.CategoryRequestDTO;
import com.globalco.jobboard.dto.response.CategoryResponseDTO;
import com.globalco.jobboard.service.CategoryService;
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
 * REST controller exposing job classification category APIs.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category Structure", description = "APIs for indexing job classification categories (e.g. Frontend, Data Science)")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Creates a new job category classification.
     *
     * @param requestDTO category payload
     * @return 201 Created containing registered category details
     */
    @PostMapping
    @Operation(summary = "Create a job category", description = "Registers a new job classification category. Name must be unique.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Category successfully created")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload details provided")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Category name already exists")
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
    @Operation(summary = "Get all categories", description = "Retrieves a flat list of all registered job categories.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categories list retrieved successfully")
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
    @Operation(summary = "Get category by ID", description = "Retrieves category details matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category successfully found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category matching the ID does not exist")
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
    @Operation(summary = "Get category by URL slug", description = "Retrieves category details matching the unique URL-friendly slug.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category successfully found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category matching the slug does not exist")
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
    @Operation(summary = "Update an existing category", description = "Modifies category metadata properties matching the given UUID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category successfully updated")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid payload details provided")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category matching the ID does not exist")
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
    @Operation(summary = "Remove category", description = "Deletes a category matching the given UUID. Cannot delete if active jobs are linked to it.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Category successfully deleted")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Category has active jobs linked")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category matching the ID does not exist")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
