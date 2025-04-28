package com.projectsync.backend.services.impl;

import com.projectsync.backend.TestDataUtil;
import com.projectsync.backend.config.MapperConfig;
import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.domain.dto.ProjectDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.mappers.impl.AccountMapperImpl;
import com.projectsync.backend.mappers.impl.ProjectMapperImpl;
import com.projectsync.backend.repositories.AccountRepository;
import com.projectsync.backend.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AccountRepository accountRepository;

    private ProjectMapperImpl projectMapper;

    private AccountMapperImpl accountMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new MapperConfig().modelMapper();
        projectMapper = new ProjectMapperImpl(modelMapper, accountRepository, null);
        accountMapper = new AccountMapperImpl(modelMapper, null, null);
        projectService = new ProjectServiceImpl(projectRepository, accountRepository, projectMapper, accountMapper);
    }

    @Test
    void findAll_ShouldReturnAllProjects() {
        // Arrange
        List<ProjectEntity> projectEntities = TestDataUtil.createProjectEntities();

        // Mock repository behavior
        when(projectRepository.findAll()).thenReturn(projectEntities);

        // Manually create expected DTOs
        List<ProjectDto> expectedProjectDtos = projectEntities.stream()
                .map(projectMapper::mapTo)
                .toList();

        // Act
        List<ProjectDto> projects = projectService.findAll();

        // Assert
        assertEquals(expectedProjectDtos.size(), projects.size());
        for (int i = 0; i < projects.size(); i++) {
            assertEquals(expectedProjectDtos.get(i).getId(), projects.get(i).getId());
            assertEquals(expectedProjectDtos.get(i).getName(), projects.get(i).getName());
        }
    }

    @Test
    void findById_WithExistingId_ShouldReturnProject() {
        // Arrange
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);

        // Mock repository behavior
        when(projectRepository.findById(projectEntity.getId())).thenReturn(Optional.of(projectEntity));

        // Act
        Optional<ProjectDto> projectDtoOptional = projectService.findById(projectEntity.getId());
        ProjectDto projectDto = projectDtoOptional.orElseThrow(() -> new AssertionError("Project not found"));

        // Assert
        assertEquals(projectDto.getId(), projectEntity.getId());
        assertEquals(projectDto.getName(), projectEntity.getName());
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Mock repository behavior
        when(projectRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act
        Optional<ProjectDto> projectDtoOptional = projectService.findById(nonExistingId);

        // Assert
        assertTrue(projectDtoOptional.isEmpty());
    }

    @Test
    void findByOwner_ShouldReturnProjectsOwnedByAccount() {
        // Arrange
        AccountEntity owner = TestDataUtil.createAccountEntities().get(0);
        List<ProjectEntity> projectEntities = TestDataUtil.createProjectEntities();

        // Mock repository behavior
        when(projectRepository.findByOwner(owner)).thenReturn(projectEntities);

        // Manually create expected DTOs
        List<ProjectDto> expectedProjectDtos = projectEntities.stream()
                .map(projectMapper::mapTo)
                .toList();

        // Act
        List<ProjectDto> projects = projectService.findByOwner(owner);

        // Assert
        assertEquals(expectedProjectDtos.size(), projects.size());
        for (int i = 0; i < projects.size(); i++) {
            assertEquals(expectedProjectDtos.get(i).getId(), projects.get(i).getId());
            assertEquals(expectedProjectDtos.get(i).getName(), projects.get(i).getName());
        }
    }

    @Test
    void findByAssignedTo_ShouldReturnProjectsAssignedToAccount() {
        // Arrange
        AccountEntity account = TestDataUtil.createAccountEntities().get(0);
        List<ProjectEntity> projectEntities = TestDataUtil.createProjectEntities();

        // Mock repository behavior
        when(projectRepository.findByAssignedTo(account)).thenReturn(projectEntities);

        // Manually create expected DTOs
        List<ProjectDto> expectedProjectDtos = projectEntities.stream()
                .map(projectMapper::mapTo)
                .toList();

        // Act
        List<ProjectDto> projects = projectService.findByAssignedTo(account);

        // Assert
        assertEquals(expectedProjectDtos.size(), projects.size());
        for (int i = 0; i < projects.size(); i++) {
            assertEquals(expectedProjectDtos.get(i).getId(), projects.get(i).getId());
            assertEquals(expectedProjectDtos.get(i).getName(), projects.get(i).getName());
        }
    }

    @Test
    void save_ShouldReturnSavedProject() {
        // Arrange
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        ProjectDto projectDto = projectMapper.mapTo(projectEntity);
        AccountEntity owner = TestDataUtil.createAccountEntities().get(0);

        // Mock repository behavior
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        // Act
        ProjectDto savedProjectDto = projectService.save(projectDto, owner);

        // Assert
        assertEquals(projectDto.getId(), savedProjectDto.getId());
        assertEquals(projectDto.getName(), savedProjectDto.getName());
    }

    @Test
    void update_WithExistingId_ShouldReturnUpdatedProject() {
        // Arrange
        UUID id = UUID.randomUUID();
        ProjectEntity existingProject = TestDataUtil.createProjectEntities().get(0);
        existingProject.setId(id);
        ProjectDto projectDto = projectMapper.mapTo(existingProject);

        // Mock repository behavior
        when(projectRepository.findById(id)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(existingProject);

        // Act
        ProjectDto updatedProjectDto = projectService.update(id, projectDto);

        // Assert
        assertEquals(id, updatedProjectDto.getId());
        assertEquals(projectDto.getName(), updatedProjectDto.getName());
    }

    @Test
    void update_WithNonExistingId_ShouldThrowException() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();
        ProjectDto projectDto = projectMapper.mapTo(TestDataUtil.createProjectEntities().get(0));

        // Mock repository behavior
        when(projectRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.update(nonExistingId, projectDto));
    }

    @Test
    void existsById_WithExistingId_ShouldReturnTrue() {
        // Arrange
        UUID existingId = UUID.randomUUID();

        // Mock repository behavior
        when(projectRepository.existsById(existingId)).thenReturn(true);

        // Act
        boolean exists = projectService.existsById(existingId);

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsById_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Mock repository behavior
        when(projectRepository.existsById(nonExistingId)).thenReturn(false);

        // Act
        boolean exists = projectService.existsById(nonExistingId);

        // Assert
        assertFalse(exists);
    }

    @Test
    void deleteById_ShouldDeleteProject() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act - this should not throw any exception
        projectService.deleteById(id);

        // No explicit assertion needed as we're just verifying the method doesn't throw an exception
    }

    @Test
    void isOwner_WhenAccountIsOwner_ShouldReturnTrue() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        AccountEntity account = TestDataUtil.createAccountEntities().get(0);

        // Mock repository behavior
        when(projectRepository.isOwner(projectId, account)).thenReturn(true);

        // Act
        boolean isOwner = projectService.isOwner(projectId, account);

        // Assert
        assertTrue(isOwner);
    }

    @Test
    void isOwner_WhenAccountIsNotOwner_ShouldReturnFalse() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        AccountEntity account = TestDataUtil.createAccountEntities().get(0);

        // Mock repository behavior
        when(projectRepository.isOwner(projectId, account)).thenReturn(false);

        // Act
        boolean isOwner = projectService.isOwner(projectId, account);

        // Assert
        assertFalse(isOwner);
    }

    @Test
    void isAssignedTo_WhenAccountIsAssigned_ShouldReturnTrue() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        AccountEntity account = TestDataUtil.createAccountEntities().get(0);

        // Mock repository behavior
        when(projectRepository.isAssignedTo(projectId, account)).thenReturn(true);

        // Act
        boolean isAssigned = projectService.isAssignedTo(projectId, account);

        // Assert
        assertTrue(isAssigned);
    }

    @Test
    void isAssignedTo_WhenAccountIsNotAssigned_ShouldReturnFalse() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        AccountEntity account = TestDataUtil.createAccountEntities().get(0);

        // Mock repository behavior
        when(projectRepository.isAssignedTo(projectId, account)).thenReturn(false);

        // Act
        boolean isAssigned = projectService.isAssignedTo(projectId, account);

        // Assert
        assertFalse(isAssigned);
    }

    @Test
    void findAccountsNotInProject_ShouldReturnAccountsNotInProject() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        List<AccountEntity> accountEntities = TestDataUtil.createAccountEntities();

        // Mock repository behavior
        when(accountRepository.findAccountsNotInProject(projectId)).thenReturn(accountEntities);

        // Manually create expected DTOs
        List<AccountDto> expectedAccountDtos = accountEntities.stream()
                .map(accountMapper::mapTo)
                .toList();

        // Act
        List<AccountDto> accounts = projectService.findAccountsNotInProject(projectId);

        // Assert
        assertEquals(expectedAccountDtos.size(), accounts.size());
        for (int i = 0; i < accounts.size(); i++) {
            assertEquals(expectedAccountDtos.get(i).getId(), accounts.get(i).getId());
            assertEquals(expectedAccountDtos.get(i).getEmail(), accounts.get(i).getEmail());
        }
    }

    @Test
    void addAssignedUser_WithValidIds_ShouldAddUserToProject() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(0);

        // Mock repository behavior
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        // Act
        ProjectDto result = projectService.addAssignedUser(projectId, accountId);

        // Assert
        assertEquals(projectMapper.mapTo(projectEntity).getId(), result.getId());
    }

    @Test
    void addAssignedUser_WithInvalidProjectId_ShouldThrowException() {
        // Arrange
        UUID invalidProjectId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        // Mock repository behavior
        when(projectRepository.findById(invalidProjectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.addAssignedUser(invalidProjectId, accountId));
    }

    @Test
    void addAssignedUser_WithInvalidAccountId_ShouldThrowException() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID invalidAccountId = UUID.randomUUID();
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);

        // Mock repository behavior
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(accountRepository.findById(invalidAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.addAssignedUser(projectId, invalidAccountId));
    }

    @Test
    void removeAssignedUser_WithValidIds_ShouldRemoveUserFromProject() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(1); // Not the owner
        projectEntity.setAssignedTo(new HashSet<>());
        projectEntity.getAssignedTo().add(accountEntity);

        // Mock repository behavior
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        // Act
        ProjectDto result = projectService.removeAssignedUser(projectId, accountId);

        // Assert
        assertEquals(projectMapper.mapTo(projectEntity).getId(), result.getId());
    }

    @Test
    void removeAssignedUser_WithOwnerAccount_ShouldThrowException() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        AccountEntity ownerEntity = projectEntity.getOwner();

        // Mock repository behavior
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(accountRepository.findById(ownerId)).thenReturn(Optional.of(ownerEntity));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.removeAssignedUser(projectId, ownerId));
    }

    @Test
    void removeAssignedUser_WithInvalidProjectId_ShouldThrowException() {
        // Arrange
        UUID invalidProjectId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        // Mock repository behavior
        when(projectRepository.findById(invalidProjectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.removeAssignedUser(invalidProjectId, accountId));
    }

    @Test
    void removeAssignedUser_WithInvalidAccountId_ShouldThrowException() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        UUID invalidAccountId = UUID.randomUUID();
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);

        // Mock repository behavior
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(accountRepository.findById(invalidAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.removeAssignedUser(projectId, invalidAccountId));
    }
}
