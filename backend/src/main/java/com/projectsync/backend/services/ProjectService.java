package com.projectsync.backend.services;

import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.domain.dto.ProjectDto;
import com.projectsync.backend.domain.entities.AccountEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectService {
    List<ProjectDto> findAll();

    Optional<ProjectDto> findById(UUID id);

    List<ProjectDto> findByOwner(AccountEntity owner);

    List<ProjectDto> findByAssignedTo(AccountEntity account);

    ProjectDto save(ProjectDto projectDto, AccountEntity owner);

    ProjectDto update(UUID id, ProjectDto projectDto);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    boolean isOwner(UUID projectId, AccountEntity account);

    boolean isAssignedTo(UUID projectId, AccountEntity account);

    ProjectDto addAssignedUser(UUID projectId, UUID accountId);

    ProjectDto removeAssignedUser(UUID projectId, UUID accountId);

    List<AccountDto> findAccountsNotInProject(UUID projectId);
}
