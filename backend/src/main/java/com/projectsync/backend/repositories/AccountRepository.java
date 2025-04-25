package com.projectsync.backend.repositories;

import com.projectsync.backend.domain.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findByEmail(String email);

    @Query("SELECT a FROM AccountEntity a WHERE a NOT IN (SELECT at FROM ProjectEntity p JOIN p.assignedTo at WHERE p.id = :projectId)")
    List<AccountEntity> findAccountsNotInProject(@Param("projectId") UUID projectId);
}
