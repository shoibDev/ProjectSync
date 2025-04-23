package com.projectsync.backend.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDto {

    private UUID id;

    private UUID ownerId;

    private String name;

    private Set<UUID> assignedToIds;

    private List<UUID> ticketIds;

}
