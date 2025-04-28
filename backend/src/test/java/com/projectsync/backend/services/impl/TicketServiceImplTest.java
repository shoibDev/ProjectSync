package com.projectsync.backend.services.impl;

import com.projectsync.backend.TestDataUtil;
import com.projectsync.backend.config.MapperConfig;
import com.projectsync.backend.domain.dto.TicketDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import com.projectsync.backend.mappers.impl.TicketMapperImpl;
import com.projectsync.backend.repositories.AccountRepository;
import com.projectsync.backend.repositories.ProjectRepository;
import com.projectsync.backend.repositories.TicketRepository;
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
public class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProjectRepository projectRepository;

    private TicketMapperImpl ticketMapper;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new MapperConfig().modelMapper();
        ticketMapper = new TicketMapperImpl(modelMapper, accountRepository);
        ticketService = new TicketServiceImpl(ticketRepository, accountRepository, projectRepository, ticketMapper);
    }

    @Test
    void findAll_ShouldReturnAllTickets() {
        // Arrange
        List<TicketEntity> ticketEntities = TestDataUtil.createTicketEntities();

        // Mock repository behavior
        when(ticketRepository.findAll()).thenReturn(ticketEntities);

        // Manually create expected DTOs
        List<TicketDto> expectedTicketDtos = ticketEntities.stream()
                .map(ticketMapper::mapTo)
                .toList();

        // Act
        List<TicketDto> tickets = ticketService.findAll();

        // Assert
        assertEquals(expectedTicketDtos.size(), tickets.size());
        for (int i = 0; i < tickets.size(); i++) {
            assertEquals(expectedTicketDtos.get(i).getId(), tickets.get(i).getId());
            assertEquals(expectedTicketDtos.get(i).getTitle(), tickets.get(i).getTitle());
        }
    }

    @Test
    void findById_WithExistingId_ShouldReturnTicket() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);

        // Mock repository behavior
        when(ticketRepository.findById(ticketEntity.getId())).thenReturn(Optional.of(ticketEntity));

        // Act
        Optional<TicketDto> ticketDtoOptional = ticketService.findById(ticketEntity.getId());
        TicketDto ticketDto = ticketDtoOptional.orElseThrow(() -> new AssertionError("Ticket not found"));

        // Assert
        assertEquals(ticketDto.getId(), ticketEntity.getId());
        assertEquals(ticketDto.getTitle(), ticketEntity.getTitle());
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Mock repository behavior
        when(ticketRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act
        Optional<TicketDto> ticketDtoOptional = ticketService.findById(nonExistingId);

        // Assert
        assertTrue(ticketDtoOptional.isEmpty());
    }

    @Test
    void findByProject_ShouldReturnTicketsInProject() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        List<TicketEntity> ticketEntities = TestDataUtil.createTicketEntities();

        // Mock repository behavior
        when(ticketRepository.findByProjectId(projectId)).thenReturn(ticketEntities);

        // Manually create expected DTOs
        List<TicketDto> expectedTicketDtos = ticketEntities.stream()
                .map(ticketMapper::mapTo)
                .toList();

        // Act
        List<TicketDto> tickets = ticketService.findByProject(projectId);

        // Assert
        assertEquals(expectedTicketDtos.size(), tickets.size());
        for (int i = 0; i < tickets.size(); i++) {
            assertEquals(expectedTicketDtos.get(i).getId(), tickets.get(i).getId());
            assertEquals(expectedTicketDtos.get(i).getTitle(), tickets.get(i).getTitle());
        }
    }

    @Test
    void findByAssignedTo_ShouldReturnTicketsAssignedToAccount() {
        // Arrange
        AccountEntity account = TestDataUtil.createAccountEntities().get(0);
        List<TicketEntity> ticketEntities = TestDataUtil.createTicketEntities();

        // Mock repository behavior
        when(ticketRepository.findByAssignedTo(account)).thenReturn(ticketEntities);

        // Manually create expected DTOs
        List<TicketDto> expectedTicketDtos = ticketEntities.stream()
                .map(ticketMapper::mapTo)
                .toList();

        // Act
        List<TicketDto> tickets = ticketService.findByAssignedTo(account);

        // Assert
        assertEquals(expectedTicketDtos.size(), tickets.size());
        for (int i = 0; i < tickets.size(); i++) {
            assertEquals(expectedTicketDtos.get(i).getId(), tickets.get(i).getId());
            assertEquals(expectedTicketDtos.get(i).getTitle(), tickets.get(i).getTitle());
        }
    }

    @Test
    void save_WithValidProjectId_ShouldReturnSavedTicket() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        TicketDto ticketDto = ticketMapper.mapTo(ticketEntity);
        ticketDto.setProjectId(projectId);
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);

        // Mock repository behavior
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(projectEntity));
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketEntity);

        // Act
        TicketDto savedTicketDto = ticketService.save(ticketDto);

        // Assert
        assertEquals(ticketDto.getId(), savedTicketDto.getId());
        assertEquals(ticketDto.getTitle(), savedTicketDto.getTitle());
    }

    @Test
    void save_WithInvalidProjectId_ShouldThrowException() {
        // Arrange
        UUID invalidProjectId = UUID.randomUUID();
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        TicketDto ticketDto = ticketMapper.mapTo(ticketEntity);
        ticketDto.setProjectId(invalidProjectId);

        // Mock repository behavior
        when(projectRepository.findById(invalidProjectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ticketService.save(ticketDto));
    }

    @Test
    void update_WithExistingId_ShouldReturnUpdatedTicket() {
        // Arrange
        UUID id = UUID.randomUUID();
        TicketEntity existingTicket = TestDataUtil.createTicketEntities().get(0);
        existingTicket.setId(id);
        TicketDto ticketDto = ticketMapper.mapTo(existingTicket);

        // Mock repository behavior
        when(ticketRepository.findById(id)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(existingTicket);

        // Act
        TicketDto updatedTicketDto = ticketService.update(id, ticketDto);

        // Assert
        assertEquals(id, updatedTicketDto.getId());
        assertEquals(ticketDto.getTitle(), updatedTicketDto.getTitle());
    }

    @Test
    void update_WithNonExistingId_ShouldThrowException() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();
        TicketDto ticketDto = ticketMapper.mapTo(TestDataUtil.createTicketEntities().get(0));

        // Mock repository behavior
        when(ticketRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ticketService.update(nonExistingId, ticketDto));
    }

    @Test
    void update_WithInvalidProjectId_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID invalidProjectId = UUID.randomUUID();
        TicketEntity existingTicket = TestDataUtil.createTicketEntities().get(0);
        existingTicket.setId(id);
        TicketDto ticketDto = ticketMapper.mapTo(existingTicket);
        ticketDto.setProjectId(invalidProjectId);

        // Mock repository behavior
        when(ticketRepository.findById(id)).thenReturn(Optional.of(existingTicket));
        when(projectRepository.findById(invalidProjectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ticketService.update(id, ticketDto));
    }

    @Test
    void existsById_WithExistingId_ShouldReturnTrue() {
        // Arrange
        UUID existingId = UUID.randomUUID();

        // Mock repository behavior
        when(ticketRepository.existsById(existingId)).thenReturn(true);

        // Act
        boolean exists = ticketService.existsById(existingId);

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsById_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        UUID nonExistingId = UUID.randomUUID();

        // Mock repository behavior
        when(ticketRepository.existsById(nonExistingId)).thenReturn(false);

        // Act
        boolean exists = ticketService.existsById(nonExistingId);

        // Assert
        assertFalse(exists);
    }

    @Test
    void deleteById_ShouldDeleteTicket() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act - this should not throw any exception
        ticketService.deleteById(id);

        // No explicit assertion needed as we're just verifying the method doesn't throw an exception
    }

    @Test
    void addAssignedUser_WithValidIds_ShouldAddUserToTicket() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(0);

        // Mock repository behavior
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketEntity));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketEntity);

        // Act
        TicketDto result = ticketService.addAssignedUser(ticketId, accountId);

        // Assert
        assertEquals(ticketMapper.mapTo(ticketEntity).getId(), result.getId());
    }

    @Test
    void addAssignedUser_WithInvalidTicketId_ShouldThrowException() {
        // Arrange
        UUID invalidTicketId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        // Mock repository behavior
        when(ticketRepository.findById(invalidTicketId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ticketService.addAssignedUser(invalidTicketId, accountId));
    }

    @Test
    void addAssignedUser_WithInvalidAccountId_ShouldThrowException() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID invalidAccountId = UUID.randomUUID();
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);

        // Mock repository behavior
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketEntity));
        when(accountRepository.findById(invalidAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ticketService.addAssignedUser(ticketId, invalidAccountId));
    }

    @Test
    void removeAssignedUser_WithValidIds_ShouldRemoveUserFromTicket() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(0);
        ticketEntity.setAssignedTo(new HashSet<>());
        ticketEntity.getAssignedTo().add(accountEntity);

        // Mock repository behavior
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketEntity));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketEntity);

        // Act
        TicketDto result = ticketService.removeAssignedUser(ticketId, accountId);

        // Assert
        assertEquals(ticketMapper.mapTo(ticketEntity).getId(), result.getId());
    }

    @Test
    void removeAssignedUser_WithInvalidTicketId_ShouldThrowException() {
        // Arrange
        UUID invalidTicketId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        // Mock repository behavior
        when(ticketRepository.findById(invalidTicketId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ticketService.removeAssignedUser(invalidTicketId, accountId));
    }

    @Test
    void removeAssignedUser_WithInvalidAccountId_ShouldThrowException() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID invalidAccountId = UUID.randomUUID();
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);

        // Mock repository behavior
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketEntity));
        when(accountRepository.findById(invalidAccountId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ticketService.removeAssignedUser(ticketId, invalidAccountId));
    }

    @Test
    void isInProject_WhenTicketIsInProject_ShouldReturnTrue() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        // Mock repository behavior
        when(ticketRepository.isInProject(ticketId, projectId)).thenReturn(true);

        // Act
        boolean isInProject = ticketService.isInProject(ticketId, projectId);

        // Assert
        assertTrue(isInProject);
    }

    @Test
    void isInProject_WhenTicketIsNotInProject_ShouldReturnFalse() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        // Mock repository behavior
        when(ticketRepository.isInProject(ticketId, projectId)).thenReturn(false);

        // Act
        boolean isInProject = ticketService.isInProject(ticketId, projectId);

        // Assert
        assertFalse(isInProject);
    }
}
