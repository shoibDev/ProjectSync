package com.projectsync.backend.services;

import com.projectsync.backend.domain.dto.TicketDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketService {
    List<TicketDto> findAll();

    Optional<TicketDto> findById(UUID id);

    List<TicketDto> findByProject(UUID projectId);

    List<TicketDto> findByAssignedTo(AccountEntity account);

    TicketDto save(TicketDto ticketDto);

    TicketDto update(UUID id, TicketDto ticketDto);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    TicketDto addAssignedUser(UUID ticketId, UUID accountId);

    TicketDto removeAssignedUser(UUID ticketId, UUID accountId);

    boolean isInProject(UUID ticketId, UUID projectId);
}
