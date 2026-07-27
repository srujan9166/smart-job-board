package com.globalco.jobboard.repository.specification;

import com.globalco.jobboard.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Dynamic specifications query builder for {@link Job} filtering.
 */
public class JobSpecification {

    /**
     * Builds predicates for search keyword, category, company, location, type, experience tier, and salaries.
     *
     * @param keyword search keyword
     * @param categoryId category identifier
     * @param companyId company identifier
     * @param location location query
     * @param jobType employment type
     * @param experienceLevel experience level
     * @param salaryMin minimum salary range
     * @param salaryMax maximum salary range
     * @param status job lifecycle status (normally ACTIVE)
     * @return Specification predicate builder
     */
    public static Specification<Job> filterJobs(
            String keyword,
            UUID categoryId,
            UUID companyId,
            String location,
            JobType jobType,
            ExperienceLevel experienceLevel,
            BigDecimal salaryMin,
            BigDecimal salaryMax,
            JobStatus status) {

        return (Root<Job> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (companyId != null) {
                predicates.add(cb.equal(root.get("company").get("id"), companyId));
            }

            if (location != null && !location.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
            }

            if (jobType != null) {
                predicates.add(cb.equal(root.get("jobType"), jobType));
            }

            if (experienceLevel != null) {
                predicates.add(cb.equal(root.get("experienceLevel"), experienceLevel));
            }

            if (salaryMin != null) {
                predicates.add(cb.ge(root.get("salaryMin"), salaryMin));
            }

            if (salaryMax != null) {
                predicates.add(cb.le(root.get("salaryMax"), salaryMax));
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.toLowerCase() + "%";

                Join<Job, Company> companyJoin = root.join("company", JoinType.LEFT);
                Join<Job, Category> categoryJoin = root.join("category", JoinType.LEFT);
                Join<Job, JobSkill> jobSkillsJoin = root.join("jobSkills", JoinType.LEFT);
                Join<JobSkill, Skill> skillJoin = jobSkillsJoin.join("skill", JoinType.LEFT);

                Predicate titlePred = cb.like(cb.lower(root.get("title")), searchPattern);
                Predicate descPred = cb.like(cb.lower(root.get("description")), searchPattern);
                Predicate companyPred = cb.like(cb.lower(companyJoin.get("name")), searchPattern);
                Predicate categoryPred = cb.like(cb.lower(categoryJoin.get("name")), searchPattern);
                Predicate locationPred = cb.like(cb.lower(root.get("location")), searchPattern);
                Predicate skillPred = cb.like(cb.lower(skillJoin.get("name")), searchPattern);

                predicates.add(cb.or(titlePred, descPred, companyPred, categoryPred, locationPred, skillPred));
                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
