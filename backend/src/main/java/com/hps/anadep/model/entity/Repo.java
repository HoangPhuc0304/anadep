package com.hps.anadep.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "repository")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Repo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private Long githubRepoId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String fullName;

    private String owner;

    private boolean isPublic;

    private String githubUrl;

    private String defaultBranch;

    private String language;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_repo",
            joinColumns = @JoinColumn(name = "repo_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repo repo = (Repo) o;
        return Objects.equals(id, repo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
