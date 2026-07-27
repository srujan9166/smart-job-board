package com.globalco.jobboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entity mapping the "job_skills" junction table. Links required/preferred
 * skills to specific job openings.
 */
@Entity
@Table(name = "job_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSkill {

    @EmbeddedId
    private JobSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("jobId")
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @NotNull(message = "Skill importance is required")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "importance", nullable = false, length = 20)
    private SkillImportance importance = SkillImportance.REQUIRED;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobSkill other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
