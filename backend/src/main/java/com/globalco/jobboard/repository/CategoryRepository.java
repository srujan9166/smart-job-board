package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Category} entity persistence.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    /**
     * Finds a category by its exact name.
     *
     * @param name category name
     * @return an Optional containing the found category, or empty
     */
    Optional<Category> findByName(String name);

    /**
     * Finds a category by its URL-friendly slug.
     *
     * @param slug category slug
     * @return an Optional containing the found category, or empty
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Checks if a category exists with the given name.
     *
     * @param name category name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Checks if a category exists with the given slug.
     *
     * @param slug category slug
     * @return true if exists, false otherwise
     */
    boolean existsBySlug(String slug);
}
