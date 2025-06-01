package com.projectsync.backend.services.impl;

import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.domain.dto.ProjectDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.mappers.impl.AccountMapperImpl;
import com.projectsync.backend.mappers.impl.ProjectMapperImpl;
import com.projectsync.backend.repositories.AccountRepository;
import com.projectsync.backend.repositories.ProjectRepository;
import com.projectsync.backend.services.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;
    private final ProjectMapperImpl projectMapper;
    private final AccountMapperImpl accountMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, AccountRepository accountRepository, 
                             ProjectMapperImpl projectMapper, AccountMapperImpl accountMapper) {
        this.projectRepository = projectRepository;
        this.accountRepository = accountRepository;
        this.projectMapper = projectMapper;
        this.accountMapper = accountMapper;
    }

    @Override
    public List<ProjectDto> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectDto> findById(UUID id) {
        return projectRepository.findById(id)
                .map(projectMapper::mapTo);
    }

    @Override
    public List<ProjectDto> findByOwner(AccountEntity owner) {
        return projectRepository.findByOwner(owner)
                .stream()
                .map(projectMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDto> findByAssignedTo(AccountEntity account) {
        return projectRepository.findByAssignedTo(account)
                .stream()
                .map(projectMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDto> findByOwnerOrAssignedTo(AccountEntity account) {
        // Get projects owned by the user
        List<ProjectDto> ownedProjects = findByOwner(account);

        // Get projects assigned to the user
        List<ProjectDto> assignedProjects = findByAssignedTo(account);

        // Combine the lists and remove duplicates
        return Stream.concat(ownedProjects.stream(), assignedProjects.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectDto save(ProjectDto projectDto, AccountEntity owner) {
        ProjectEntity projectEntity = projectMapper.mapFrom(projectDto);
        projectEntity.setOwner(owner);

        if (projectEntity.getAssignedTo() == null) {
            projectEntity.setAssignedTo(new HashSet<>());
        }
        // Always add the owner to the assigned users
        projectEntity.getAssignedTo().add(owner);

        ProjectEntity savedEntity = projectRepository.save(projectEntity);
        return projectMapper.mapTo(savedEntity);
    }

    @Override
    @Transactional
    public ProjectDto update(UUID id, ProjectDto projectDto) {
        return projectRepository.findById(id)
                .map(existingProject -> {
                    projectDto.setId(id);
                    ProjectEntity updatedEntity = projectMapper.mapFrom(projectDto);
                    updatedEntity.setOwner(existingProject.getOwner()); // Preserve the original owner

                    if (updatedEntity.getAssignedTo() == null) {
                        updatedEntity.setAssignedTo(new HashSet<>());
                    }
                    // Always ensure the owner is in the assigned users
                    updatedEntity.getAssignedTo().add(existingProject.getOwner());

                    return projectMapper.mapTo(projectRepository.save(updatedEntity));
                })
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + id));
    }

    @Override
    public boolean existsById(UUID id) {
        return projectRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        projectRepository.deleteById(id);
    }

    @Override
    public boolean isOwner(UUID projectId, AccountEntity account) {
        return projectRepository.isOwner(projectId, account);
    }

    @Override
    public boolean isAssignedTo(UUID projectId, AccountEntity account) {
        return projectRepository.isAssignedTo(projectId, account);
    }

    @Override
    public List<AccountDto> findAccountsNotInProject(UUID projectId) {
        return accountRepository.findAccountsNotInProject(projectId)
                .stream()
                .map(accountMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectDto addAssignedUser(UUID projectId, UUID accountId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));

        if (project.getAssignedTo() == null) {
            project.setAssignedTo(new HashSet<>());
        }

        project.getAssignedTo().add(account);
        return projectMapper.mapTo(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectDto removeAssignedUser(UUID projectId, UUID accountId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));

        // Don't remove the owner from assigned users
        if (project.getOwner().equals(account)) {
            throw new IllegalArgumentException("Cannot remove the owner from assigned users");
        }

        if (project.getAssignedTo() != null) {
            project.getAssignedTo().remove(account);
            return projectMapper.mapTo(projectRepository.save(project));
        }

        return projectMapper.mapTo(project);
    }
}
