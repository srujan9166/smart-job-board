package com.globalco.jobboard.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity mapping the "seeker_skills" junction table. Represents skills possessed
 * by job candidates, detailing their proficiency level.
 */
@Entity
@Table(name = "seeker_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeekerSkill {

    @EmbeddedId
    private SeekerSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("seekerId")
    @JoinColumn(name = "seeker_id")
    private SeekerProfile seeker;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", length = 20)
    private ProficiencyLevel proficiencyLevel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeekerSkill other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
