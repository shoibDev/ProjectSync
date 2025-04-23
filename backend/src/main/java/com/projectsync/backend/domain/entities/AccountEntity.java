package com.projectsync.backend.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "owner")
    private List<ProjectEntity> projects;

    @ManyToMany(mappedBy = "assignedTo")
    private List<TicketEntity> assignedTickets;
}
