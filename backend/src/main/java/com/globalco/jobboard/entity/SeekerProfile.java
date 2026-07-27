package com.globalco.jobboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity mapping the "seeker_profiles" table. Holds resume links, bio, and
 * portfolios for Job Seekers.
 */
@Entity
@Table(name = "seeker_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeekerProfile {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Size(max = 500)
    @Column(name = "resume_url")
    private String resumeUrl;

    @Size(max = 255)
    @URL(message = "GitHub URL must be valid")
    @Column(name = "github_url")
    private String githubUrl;

    @Size(max = 255)
    @URL(message = "LinkedIn URL must be valid")
    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Size(max = 255)
    @URL(message = "Portfolio URL must be valid")
    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "seeker", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SeekerSkill> seekerSkills = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "seeker", fetch = FetchType.LAZY)
    private List<Application> applications = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeekerProfile other)) return false;
        return userId != null && userId.equals(other.getUserId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
