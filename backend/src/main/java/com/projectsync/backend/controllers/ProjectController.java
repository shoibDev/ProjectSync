package com.projectsync.backend.controllers;

import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.domain.dto.ProjectDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.mappers.impl.ProjectMapperImpl;
import com.projectsync.backend.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapperImpl projectMapper;

    public ProjectController(ProjectService projectService, ProjectMapperImpl projectMapper){
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects(@AuthenticationPrincipal AccountEntity principal) {
        List<ProjectDto> projects = projectService.findByOwnerOrAssignedTo(principal);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable UUID id) {
        Optional<ProjectDto> project = projectService.findById(id);
        return project.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my-projects")
    public ResponseEntity<List<ProjectDto>> getMyProjects(@AuthenticationPrincipal AccountEntity principal) {
        List<ProjectDto> projects = projectService.findByOwner(principal);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/assigned-to-me")
    public ResponseEntity<List<ProjectDto>> getProjectsAssignedToMe(@AuthenticationPrincipal AccountEntity principal) {
        List<ProjectDto> projects = projectService.findByAssignedTo(principal);
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @RequestBody ProjectDto projectDto,
            @AuthenticationPrincipal AccountEntity principal) {
        ProjectDto createdProject = projectService.save(projectDto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable UUID id,
            @RequestBody ProjectDto projectDto,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the user is the owner of the project
        if (!projectService.isOwner(id, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ProjectDto updatedProject = projectService.update(id, projectDto);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable UUID id,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the user is the owner of the project
        if (!projectService.isOwner(id, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!projectService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/assign/{accountId}")
    public ResponseEntity<ProjectDto> assignUserToProject(
            @PathVariable UUID projectId,
            @PathVariable UUID accountId,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the user is the owner of the project
        if (!projectService.isOwner(projectId, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ProjectDto updatedProject = projectService.addAssignedUser(projectId, accountId);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{projectId}/unassign/{accountId}")
    public ResponseEntity<ProjectDto> unassignUserFromProject(
            @PathVariable UUID projectId,
            @PathVariable UUID accountId,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the user is the owner of the project
        if (!projectService.isOwner(projectId, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ProjectDto updatedProject = projectService.removeAssignedUser(projectId, accountId);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping("/{projectId}/available-users")
    public ResponseEntity<List<AccountDto>> getAvailableUsers(
            @PathVariable UUID projectId,
            @AuthenticationPrincipal AccountEntity principal) {
        // Check if the user is the owner of the project
        if (!projectService.isOwner(projectId, principal)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<AccountDto> availableUsers = projectService.findAccountsNotInProject(projectId);
        return ResponseEntity.ok(availableUsers);
    }
}
