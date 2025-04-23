package com.projectsync.backend.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "tickets",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "title"})
)
public class TicketEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @ManyToMany
    @JoinTable(
            name = "ticket_assigned_to",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<AccountEntity> assignedTo;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    public enum TicketStatus {
        TODO,
        IN_PROGRESS,
        DONE,
        BLOCKED
    }
}
