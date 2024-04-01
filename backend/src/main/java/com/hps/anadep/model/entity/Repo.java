package com.hps.anadep.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long githubRepoId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String fullName;

    private String owner;

    private boolean isPublic;

    private String githubUrl;

    private String language;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
