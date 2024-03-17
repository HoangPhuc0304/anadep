package com.hps.anadep.repository;

import com.hps.anadep.model.entity.AuthToken;
import com.hps.anadep.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, String> {
    Optional<AuthToken> findByUser(User user);

    Optional<AuthToken> findByGithubToken(String githubToken);
}
