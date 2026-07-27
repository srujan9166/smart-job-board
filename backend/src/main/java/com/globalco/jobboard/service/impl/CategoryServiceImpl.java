package com.globalco.jobboard.service.impl;

import com.globalco.jobboard.dto.request.CategoryRequestDTO;
import com.globalco.jobboard.dto.response.CategoryResponseDTO;
import com.globalco.jobboard.entity.Category;
import com.globalco.jobboard.exception.DuplicateResourceException;
import com.globalco.jobboard.exception.InvalidOperationException;
import com.globalco.jobboard.exception.ResourceNotFoundException;
import com.globalco.jobboard.mapper.CategoryMapper;
import com.globalco.jobboard.repository.CategoryRepository;
import com.globalco.jobboard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CategoryService} managing job classifications.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        log.info("Attempting to create category with name: {}", dto.getName());

        if (categoryRepository.existsByName(dto.getName())) {
            log.warn("Category creation failed - name already registered: {}", dto.getName());
            throw new DuplicateResourceException("A category with name " + dto.getName() + " already exists.");
        }

        Category category = categoryMapper.toEntity(dto);
        Category savedCategory = categoryRepository.save(category);

        log.info("Successfully created category with ID: {}", savedCategory.getId());
        return categoryMapper.toResponseDTO(savedCategory);
    }

    @Override
    public CategoryResponseDTO getCategoryById(UUID id) {
        log.debug("Retrieving category by ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    public CategoryResponseDTO getCategoryBySlug(String slug) {
        log.debug("Retrieving category by slug: {}", slug);
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with slug: " + slug));
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        log.debug("Retrieving all categories");
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(UUID id, CategoryRequestDTO dto) {
        log.info("Updating category with ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // Check name uniqueness if name is changed
        if (!category.getName().equalsIgnoreCase(dto.getName()) && categoryRepository.existsByName(dto.getName())) {
            log.warn("Category update failed - name already registered: {}", dto.getName());
            throw new DuplicateResourceException("A category with name " + dto.getName() + " already exists.");
        }

        categoryMapper.updateEntity(dto, category);
        Category updatedCategory = categoryRepository.save(category);

        log.info("Successfully updated category with ID: {}", updatedCategory.getId());
        return categoryMapper.toResponseDTO(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        log.info("Deleting category with ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        // Business rule constraint: cannot delete category if it has active jobs
        if (category.getJobs() != null && !category.getJobs().isEmpty()) {
            throw new InvalidOperationException("Cannot delete category because it has linked jobs.");
        }

        categoryRepository.delete(category);
        log.info("Successfully deleted category with ID: {}", id);
    }
}
