package com.globalco.jobboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity mapping the "companies" table. Represesents corporate employers
 * posting jobs in the board.
 */
@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Company name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Slug is required")
    @Size(max = 255)
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Size(max = 255)
    @Column(name = "website")
    private String website;

    @Size(max = 500)
    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Size(max = 100)
    @Column(name = "industry")
    private String industry;

    @Column(name = "founded_date")
    private LocalDate foundedDate;

    @Size(max = 255)
    @Column(name = "headquarters")
    private String headquarters;

    @NotNull(message = "Creator is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<RecruiterProfile> recruiterProfiles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Job> jobs = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
