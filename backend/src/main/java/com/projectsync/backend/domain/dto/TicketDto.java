package com.projectsync.backend.domain.dto;

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
public class TicketDto {

    private UUID id;

    private String title;

    private String description;

    private TicketDto.TicketStatus status;

    private TicketDto.TicketPriority priority;

    private TicketDto.TicketType type;

    private Set<UUID> assignedToIds;

    private UUID projectId;

    public enum TicketStatus {
        TODO,
        IN_PROGRESS,
        DONE,
        BLOCKED
    }

    public enum TicketPriority {
        LOW,
        MEDIUM,
        HIGH
    }

    public enum TicketType {
        BUG,
        FEATURE,
        QUESTION,
        TASK
    }
}
