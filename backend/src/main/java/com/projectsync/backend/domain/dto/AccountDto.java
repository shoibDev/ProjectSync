package com.projectsync.backend.domain.dto;

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
public class AccountDto {

    private UUID id;

    private String email;

    private List<UUID> projectIds;

    private List<UUID> assignedTicketIds;

}
