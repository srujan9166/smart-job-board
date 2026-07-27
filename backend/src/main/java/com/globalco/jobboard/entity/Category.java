package com.globalco.jobboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity mapping the "categories" table. Groups job postings and skills under
 * business categories (e.g. Software Engineering).
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Category name is required")
    @Size(max = 100)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Slug is required")
    @Size(max = 100)
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Skill> skills = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Job> jobs = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
