package com.globalco.jobboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity mapping the "jobs" table. Contains listing information, location,
 * requirements, and candidate matching skill vectors.
 */
@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Company is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotNull(message = "Category is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NotNull(message = "Poster (User) is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;

    @NotBlank(message = "Job title is required")
    @Size(max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Description is required")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "responsibilities", columnDefinition = "TEXT")
    private String responsibilities;

    @NotBlank(message = "Location is required")
    @Size(max = 255)
    @Column(name = "location", nullable = false)
    private String location;

    @NotNull(message = "Job type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false, length = 50)
    private JobType jobType;

    @NotNull(message = "Experience level is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", nullable = false, length = 50)
    private ExperienceLevel experienceLevel;

    @PositiveOrZero(message = "Minimum salary must be greater than or equal to 0")
    @Column(name = "salary_min", precision = 15, scale = 2)
    private BigDecimal salaryMin;

    @PositiveOrZero(message = "Maximum salary must be greater than or equal to 0")
    @Column(name = "salary_max", precision = 15, scale = 2)
    private BigDecimal salaryMax;

    @NotBlank(message = "Currency is required")
    @Size(max = 3)
    @Builder.Default
    @Column(name = "currency", nullable = false, length = 3)
    private String currency = "USD";

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false, length = 20)
    private JobStatus status = JobStatus.DRAFT;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<JobSkill> jobSkills = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<Application> applications = new ArrayList<>();

    @AssertTrue(message = "Maximum salary must be greater than or equal to minimum salary")
    public boolean isSalaryRangeValid() {
        if (salaryMin == null || salaryMax == null) {
            return true;
        }
        return salaryMax.compareTo(salaryMin) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Job other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
