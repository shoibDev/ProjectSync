package com.projectsync.backend.controllers;

import com.projectsync.backend.domain.dto.TicketDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.services.AccountService;
import com.projectsync.backend.services.ProjectService;
import com.projectsync.backend.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final ProjectService projectService;

    private final AccountService accountService;

    public TicketController(TicketService ticketService, ProjectService projectService, 
                           AccountService accountService) {
        this.ticketService = ticketService;
        this.projectService = projectService;
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        List<TicketDto> tickets = ticketService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable UUID id) {
        Optional<TicketDto> ticket = ticketService.findById(id);
        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TicketDto>> getTicketsByProject(
            @PathVariable UUID projectId,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the user is assigned to the project
        if (!projectService.isAssignedTo(projectId, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<TicketDto> tickets = ticketService.findByProject(projectId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/assigned-to-me")
    public ResponseEntity<List<TicketDto>> getTicketsAssignedToMe(
            @AuthenticationPrincipal AccountEntity principal) {
        List<TicketDto> tickets = ticketService.findByAssignedTo(principal);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(
            @RequestBody TicketDto ticketDto,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the user is assigned to the project
        if (!projectService.isAssignedTo(ticketDto.getProjectId(), principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        TicketDto createdTicket = ticketService.save(ticketDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> updateTicket(
            @PathVariable UUID id,
            @RequestBody TicketDto ticketDto,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the ticket exists
        if (!ticketService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Get the current ticket to check its project
        Optional<TicketDto> currentTicket = ticketService.findById(id);
        if (currentTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Check if the user is assigned to the project
        UUID projectId = currentTicket.get().getProjectId();
        if (!projectService.isAssignedTo(projectId, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // If the project ID is being changed, check if the user is assigned to the new project
        if (ticketDto.getProjectId() != null && !ticketDto.getProjectId().equals(projectId)) {
            if (!projectService.isAssignedTo(ticketDto.getProjectId(), principal)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        TicketDto updatedTicket = ticketService.update(id, ticketDto);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(
            @PathVariable UUID id,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the ticket exists
        if (!ticketService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Get the current ticket to check its project
        Optional<TicketDto> currentTicket = ticketService.findById(id);
        if (currentTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Check if the user is the owner of the project
        UUID projectId = currentTicket.get().getProjectId();
        if (!projectService.isOwner(projectId, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ticketId}/assign/{accountId}")
    public ResponseEntity<TicketDto> assignUserToTicket(
            @PathVariable UUID ticketId,
            @PathVariable UUID accountId,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the ticket exists
        if (!ticketService.existsById(ticketId)) {
            return ResponseEntity.notFound().build();
        }

        // Get the current ticket to check its project
        Optional<TicketDto> currentTicket = ticketService.findById(ticketId);
        if (currentTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Check if the user is assigned to the project
        UUID projectId = currentTicket.get().getProjectId();
        if (!projectService.isAssignedTo(projectId, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Check if the account exists
        if (!accountService.existsById(accountId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Account not found
        }

        // Note: We're not checking if the user to be assigned is assigned to the project
        // because the TicketService's addAssignedUser method should handle this validation

        TicketDto updatedTicket = ticketService.addAssignedUser(ticketId, accountId);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{ticketId}/unassign/{accountId}")
    public ResponseEntity<TicketDto> unassignUserFromTicket(
            @PathVariable UUID ticketId,
            @PathVariable UUID accountId,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the ticket exists
        if (!ticketService.existsById(ticketId)) {
            return ResponseEntity.notFound().build();
        }

        // Get the current ticket to check its project
        Optional<TicketDto> currentTicket = ticketService.findById(ticketId);
        if (currentTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Check if the user is assigned to the project
        UUID projectId = currentTicket.get().getProjectId();
        if (!projectService.isAssignedTo(projectId, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Check if the account exists
        if (!accountService.existsById(accountId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Account not found
        }

        // Note: We're not checking if the user to be unassigned is assigned to the project
        // because the TicketService's removeAssignedUser method should handle this validation

        TicketDto updatedTicket = ticketService.removeAssignedUser(ticketId, accountId);
        return ResponseEntity.ok(updatedTicket);
    }
}
