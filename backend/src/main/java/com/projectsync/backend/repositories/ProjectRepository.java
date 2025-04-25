package com.projectsync.backend.repositories;

import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID> {
    List<ProjectEntity> findByOwner(AccountEntity owner);

    @Query("SELECT p FROM ProjectEntity p JOIN p.assignedTo a WHERE a = :account")
    List<ProjectEntity> findByAssignedTo(@Param("account") AccountEntity account);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ProjectEntity p WHERE p.id = :projectId AND p.owner = :account")
    boolean isOwner(@Param("projectId") UUID projectId, @Param("account") AccountEntity account);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ProjectEntity p JOIN p.assignedTo a WHERE p.id = :projectId AND a = :account")
    boolean isAssignedTo(@Param("projectId") UUID projectId, @Param("account") AccountEntity account);
}
