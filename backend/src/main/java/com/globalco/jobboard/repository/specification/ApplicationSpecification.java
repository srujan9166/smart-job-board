package com.globalco.jobboard.repository.specification;

import com.globalco.jobboard.entity.Application;
import com.globalco.jobboard.entity.ApplicationStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Dynamic specifications query builder for {@link Application} filtering.
 */
public class ApplicationSpecification {

    /**
     * Builds predicates for application status, seeker, and job filters.
     *
     * @param status application pipeline status
     * @param seekerId seeker identifier
     * @param jobId job identifier
     * @return Specification predicate builder
     */
    public static Specification<Application> filterApplications(
            ApplicationStatus status,
            UUID seekerId,
            UUID jobId) {

        return (Root<Application> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (seekerId != null) {
                predicates.add(cb.equal(root.get("seeker").get("id"), seekerId));
            }

            if (jobId != null) {
                predicates.add(cb.equal(root.get("job").get("id"), jobId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
