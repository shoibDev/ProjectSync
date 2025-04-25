package com.projectsync.backend.repositories;

import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {
    List<TicketEntity> findByProject(ProjectEntity project);

    List<TicketEntity> findByProjectId(UUID projectId);

    @Query("SELECT t FROM TicketEntity t JOIN t.assignedTo a WHERE a = :account")
    List<TicketEntity> findByAssignedTo(@Param("account") AccountEntity account);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TicketEntity t WHERE t.id = :ticketId AND t.project.id = :projectId")
    boolean isInProject(@Param("ticketId") UUID ticketId, @Param("projectId") UUID projectId);
}
