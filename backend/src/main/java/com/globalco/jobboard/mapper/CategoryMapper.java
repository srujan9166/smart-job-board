package com.globalco.jobboard.mapper;

import com.globalco.jobboard.dto.request.CategoryRequestDTO;
import com.globalco.jobboard.dto.response.CategoryResponseDTO;
import com.globalco.jobboard.entity.Category;
import org.springframework.stereotype.Component;

/**
 * Mapper component converting between {@link Category} entity and DTO types.
 */
@Component
public class CategoryMapper {

    /**
     * Converts a Category entity to CategoryResponseDTO.
     *
     * @param category category entity
     * @return category response DTO
     */
    public CategoryResponseDTO toResponseDTO(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .build();
    }

    /**
     * Converts a CategoryRequestDTO to a new Category entity.
     *
     * @param dto category request DTO
     * @return category entity
     */
    public Category toEntity(CategoryRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Category.builder()
                .name(dto.getName())
                .slug(generateSlug(dto.getName()))
                .description(dto.getDescription())
                .build();
    }

    /**
     * Updates an existing Category entity with properties from CategoryRequestDTO.
     *
     * @param dto category request DTO containing updates
     * @param category target category entity to modify
     */
    public void updateEntity(CategoryRequestDTO dto, Category category) {
        if (dto == null || category == null) {
            return;
        }
        category.setName(dto.getName());
        category.setSlug(generateSlug(dto.getName()));
        category.setDescription(dto.getDescription());
    }

    /**
     * Generates a URL slug from the category name.
     */
    private String generateSlug(String name) {
        if (name == null) {
            return null;
        }
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }
}
