package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Company} entity persistence.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID>, JpaSpecificationExecutor<Company> {

    /**
     * Finds a company by its exact name.
     *
     * @param name company name
     * @return an Optional containing the found company, or empty
     */
    Optional<Company> findByName(String name);

    /**
     * Finds a company by its URL-friendly slug.
     *
     * @param slug company slug
     * @return an Optional containing the found company, or empty
     */
    Optional<Company> findBySlug(String slug);

    /**
     * Checks if a company exists with the given name.
     *
     * @param name company name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Checks if a company exists with the given URL-friendly slug.
     *
     * @param slug company slug
     * @return true if exists, false otherwise
     */
    boolean existsBySlug(String slug);
}
