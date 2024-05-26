package com.hps.anadep.repository;

import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RepoRepository extends JpaRepository<Repo, UUID> {
    Optional<Repo> findByGithubRepoId(Long githubRepoId);
}
