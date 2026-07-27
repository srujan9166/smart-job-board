package com.globalco.jobboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Composite primary key class for mapping the "seeker_skills" junction table.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SeekerSkillId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "seeker_id")
    private UUID seekerId;

    @Column(name = "skill_id")
    private UUID skillId;
}
