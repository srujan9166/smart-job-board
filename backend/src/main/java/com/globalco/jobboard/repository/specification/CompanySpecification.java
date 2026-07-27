package com.globalco.jobboard.repository.specification;

import com.globalco.jobboard.entity.Company;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic specifications query builder for {@link Company} filtering.
 */
public class CompanySpecification {

    /**
     * Builds predicates for company filtering.
     *
     * @param name company name query
     * @param location company location query
     * @return Specification predicate builder
     */
    public static Specification<Company> filterCompanies(String name, String location) {
        return (Root<Company> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (location != null && !location.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
