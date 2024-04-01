package com.hps.anadep.repository;

import com.hps.anadep.model.entity.History;
import com.hps.anadep.model.entity.Repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<History, UUID> {

    @Query(value = "SELECT h FROM History h " +
            "WHERE h.repo.id = :repoId " +
            "AND (:type IS NULL OR LOWER(h.type) LIKE CONCAT('%',LOWER(:type),'%')) " +
            "ORDER BY h.createdAt DESC ")
    List<History> findAllOrderByCreatedAtDesc(@Param("repoId") UUID repoId, @Param("type") String type);

    Optional<History> findByIdAndRepo(UUID id, Repo repo);

    void deleteAllByRepo(Repo repo);
}
