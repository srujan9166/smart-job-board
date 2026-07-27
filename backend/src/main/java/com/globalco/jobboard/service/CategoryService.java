package com.globalco.jobboard.service;

import com.globalco.jobboard.dto.request.CategoryRequestDTO;
import com.globalco.jobboard.dto.response.CategoryResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface defining category business actions.
 */
public interface CategoryService {

    /**
     * Creates a new job category classification.
     *
     * @param dto category details
     * @return response details of the registered category
     * @throws com.globalco.jobboard.exception.DuplicateResourceException if name is already taken
     */
    CategoryResponseDTO createCategory(CategoryRequestDTO dto);

    /**
     * Retrieves a category by its unique ID.
     *
     * @param id category identifier
     * @return category details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    CategoryResponseDTO getCategoryById(UUID id);

    /**
     * Retrieves a category by its URL slug.
     *
     * @param slug category slug
     * @return category details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     */
    CategoryResponseDTO getCategoryBySlug(String slug);

    /**
     * Retrieves all categories in the system.
     *
     * @return list of categories
     */
    List<CategoryResponseDTO> getAllCategories();

    /**
     * Updates an existing category's metadata.
     *
     * @param id category identifier
     * @param dto update payload
     * @return updated category details
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if category not found
     * @throws com.globalco.jobboard.exception.DuplicateResourceException if name is changed to an already registered name
     */
    CategoryResponseDTO updateCategory(UUID id, CategoryRequestDTO dto);

    /**
     * Deletes a category by its ID.
     *
     * @param id category identifier
     * @throws com.globalco.jobboard.exception.ResourceNotFoundException if not found
     * @throws com.globalco.jobboard.exception.InvalidOperationException if category has linked jobs
     */
    void deleteCategory(UUID id);
}
