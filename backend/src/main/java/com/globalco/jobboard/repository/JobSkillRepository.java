package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.JobSkill;
import com.globalco.jobboard.entity.JobSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link JobSkill} junction entity persistence.
 */
@Repository
public interface JobSkillRepository extends JpaRepository<JobSkill, JobSkillId> {

    /**
     * Finds all skills configured for a specific job post.
     *
     * @param jobId job identifier
     * @return list of job skills
     */
    List<JobSkill> findByJobId(UUID jobId);
}
