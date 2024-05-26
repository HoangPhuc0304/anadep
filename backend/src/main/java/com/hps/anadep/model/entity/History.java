package com.hps.anadep.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "history")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String scanningResult;

    @Column(columnDefinition = "TEXT")
    private String vulnerabilityResult;

    @NotEmpty
    private String type;

    @NotEmpty
    private String path;

    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "repo_id", referencedColumnName = "id")
    private Repo repo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
