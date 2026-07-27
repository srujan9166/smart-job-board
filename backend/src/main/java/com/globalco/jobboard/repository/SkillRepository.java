package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Skill} entity persistence.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {

    /**
     * Finds a skill by its exact name.
     *
     * @param name skill name
     * @return an Optional containing the found skill, or empty
     */
    Optional<Skill> findByName(String name);

    /**
     * Finds all skills belonging to a specific category.
     *
     * @param categoryId category identifier
     * @return list of matching skills
     */
    List<Skill> findByCategoryId(UUID categoryId);

    /**
     * Checks if a skill exists with the given name.
     *
     * @param name skill name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
}
