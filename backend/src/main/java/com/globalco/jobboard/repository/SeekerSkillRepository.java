package com.globalco.jobboard.repository;

import com.globalco.jobboard.entity.SeekerSkill;
import com.globalco.jobboard.entity.SeekerSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link SeekerSkill} junction entity persistence.
 */
@Repository
public interface SeekerSkillRepository extends JpaRepository<SeekerSkill, SeekerSkillId> {

    /**
     * Finds all skills configured for a specific seeker profile.
     *
     * @param seekerId seeker identifier (user_id)
     * @return list of seeker skills
     */
    List<SeekerSkill> findBySeekerUserId(UUID seekerId);
}
