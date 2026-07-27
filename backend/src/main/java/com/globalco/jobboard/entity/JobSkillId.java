package com.globalco.jobboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Composite primary key class for mapping the "job_skills" junction table.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class JobSkillId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "skill_id")
    private UUID skillId;
}
