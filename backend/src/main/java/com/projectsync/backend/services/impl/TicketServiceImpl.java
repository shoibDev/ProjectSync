package com.projectsync.backend.services.impl;

import com.projectsync.backend.domain.dto.TicketDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import com.projectsync.backend.mappers.impl.TicketMapperImpl;
import com.projectsync.backend.repositories.AccountRepository;
import com.projectsync.backend.repositories.ProjectRepository;
import com.projectsync.backend.repositories.TicketRepository;
import com.projectsync.backend.services.TicketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final TicketMapperImpl ticketMapper;

    public TicketServiceImpl(TicketRepository ticketRepository, AccountRepository accountRepository,
                           ProjectRepository projectRepository, TicketMapperImpl ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.accountRepository = accountRepository;
        this.projectRepository = projectRepository;
        this.ticketMapper = ticketMapper;
    }

    @Override
    public List<TicketDto> findAll() {
        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TicketDto> findById(UUID id) {
        return ticketRepository.findById(id)
                .map(ticketMapper::mapTo);
    }

    @Override
    public List<TicketDto> findByProject(UUID projectId) {
        return ticketRepository.findByProjectId(projectId)
                .stream()
                .map(ticketMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDto> findByAssignedTo(AccountEntity account) {
        return ticketRepository.findByAssignedTo(account)
                .stream()
                .map(ticketMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TicketDto save(TicketDto ticketDto) {
        TicketEntity ticketEntity = ticketMapper.mapFrom(ticketDto);

        // Set the project
        if (ticketDto.getProjectId() != null) {
            ProjectEntity project = projectRepository.findById(ticketDto.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + ticketDto.getProjectId()));
            ticketEntity.setProject(project);
        }

        // Initialize assignedTo if null
        if (ticketEntity.getAssignedTo() == null) {
            ticketEntity.setAssignedTo(new HashSet<>());
        }

        TicketEntity savedEntity = ticketRepository.save(ticketEntity);
        return ticketMapper.mapTo(savedEntity);
    }

    @Override
    @Transactional
    public TicketDto update(UUID id, TicketDto ticketDto) {
        return ticketRepository.findById(id)
                .map(existingTicket -> {
                    ticketDto.setId(id);
                    TicketEntity updatedEntity = ticketMapper.mapFrom(ticketDto);

                    // Set the project
                    if (ticketDto.getProjectId() != null) {
                        ProjectEntity project = projectRepository.findById(ticketDto.getProjectId())
                                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + ticketDto.getProjectId()));
                        updatedEntity.setProject(project);
                    } else {
                        updatedEntity.setProject(existingTicket.getProject());
                    }

                    // Initialize assignedTo if null
                    if (updatedEntity.getAssignedTo() == null) {
                        updatedEntity.setAssignedTo(new HashSet<>());
                    }

                    return ticketMapper.mapTo(ticketRepository.save(updatedEntity));
                })
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + id));
    }

    @Override
    public boolean existsById(UUID id) {
        return ticketRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        ticketRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TicketDto addAssignedUser(UUID ticketId, UUID accountId) {
        TicketEntity ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + ticketId));

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));

        if (ticket.getAssignedTo() == null) {
            ticket.setAssignedTo(new HashSet<>());
        }

        ticket.getAssignedTo().add(account);
        return ticketMapper.mapTo(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public TicketDto removeAssignedUser(UUID ticketId, UUID accountId) {
        TicketEntity ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found with id: " + ticketId));

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));

        if (ticket.getAssignedTo() != null) {
            ticket.getAssignedTo().remove(account);
            return ticketMapper.mapTo(ticketRepository.save(ticket));
        }

        return ticketMapper.mapTo(ticket);
    }

    @Override
    public boolean isInProject(UUID ticketId, UUID projectId) {
        return ticketRepository.isInProject(ticketId, projectId);
    }
}
