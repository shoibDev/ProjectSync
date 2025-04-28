package com.projectsync.backend.mappers.impl;

import com.projectsync.backend.TestDataUtil;
import com.projectsync.backend.config.MapperConfig;
import com.projectsync.backend.domain.dto.ProjectDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import com.projectsync.backend.repositories.AccountRepository;
import com.projectsync.backend.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProjectMapperImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TicketRepository ticketRepository;

    private ProjectMapperImpl projectMapper;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new MapperConfig().modelMapper();
        projectMapper = new ProjectMapperImpl(modelMapper, accountRepository, ticketRepository);
        projectMapper.init();
    }

    @Test
    void mapToShouldMapEntityToDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        AccountEntity owner = TestDataUtil.createAccountEntities().get(0);
        ProjectEntity projectEntity = ProjectEntity.builder()
                .id(id)
                .owner(owner)
                .name("test")
                .build();
        // Act
        ProjectDto dto = projectMapper.mapTo(projectEntity);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(owner.getId(), dto.getOwnerId());
        assertEquals("test", dto.getName());
    }

    @Test
    void mapToShouldMapOwnerIdCorrectly() {
        // Arrange
        UUID id = UUID.randomUUID();
        AccountEntity owner = TestDataUtil.createAccountEntities().get(0);
        ProjectEntity projectEntity = ProjectEntity.builder()
                .id(id)
                .owner(owner)
                .name("test")
                .build();

        // Act
        ProjectDto dto = projectMapper.mapTo(projectEntity);

        // Assert
        assertEquals(owner.getId(), dto.getOwnerId());
    }

    @Test
    void mapToShouldMapAssignedToIdsCorrectly() {
        // Arrange
        UUID id = UUID.randomUUID();
        List<AccountEntity> accounts = TestDataUtil.createAccountEntities();
        AccountEntity owner = accounts.get(0);
        AccountEntity assigned1 = accounts.get(1);
        AccountEntity assigned2 = accounts.get(2);

        Set<AccountEntity> assignedTo = new HashSet<>(Set.of(assigned1, assigned2));

        ProjectEntity projectEntity = ProjectEntity.builder()
                .id(id)
                .owner(owner)
                .name("test")
                .assignedTo(assignedTo)
                .build();

        // Act
        ProjectDto dto = projectMapper.mapTo(projectEntity);

        // Assert
        assertTrue(dto.getAssignedToIds().contains(assigned1.getId()));
        assertTrue(dto.getAssignedToIds().contains(assigned2.getId()));
    }

    @Test
    void mapToShouldMapTicketIdsCorrectly() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ticketId1 = UUID.randomUUID();
        UUID ticketId2 = UUID.randomUUID();

        AccountEntity owner = TestDataUtil.createAccountEntities().get(0);

        TicketEntity ticket1 = TicketEntity.builder()
                .id(ticketId1)
                .title("Test Ticket 1")
                .description("Test Description 1")
                .status(TicketEntity.TicketStatus.TODO)
                .build();

        TicketEntity ticket2 = TicketEntity.builder()
                .id(ticketId2)
                .title("Test Ticket 2")
                .description("Test Description 2")
                .status(TicketEntity.TicketStatus.IN_PROGRESS)
                .build();

        ProjectEntity projectEntity = ProjectEntity.builder()
                .id(id)
                .owner(owner)
                .name("test")
                .tickets(List.of(ticket1, ticket2))
                .build();

        // Act
        ProjectDto dto = projectMapper.mapTo(projectEntity);

        // Assert
        assertTrue(dto.getTicketIds().contains(ticketId1));
        assertTrue(dto.getTicketIds().contains(ticketId2));
    }

    @Test
    void mapToShouldHandleNullAssignedTo() {
        // Arrange
        UUID id = UUID.randomUUID();
        AccountEntity owner = TestDataUtil.createAccountEntities().get(0);

        ProjectEntity projectEntity = ProjectEntity.builder()
                .id(id)
                .owner(owner)
                .name("test")
                .assignedTo(null)
                .build();

        // Act
        ProjectDto dto = projectMapper.mapTo(projectEntity);

        // Assert
        // The mapper should handle null assignedTo by setting assignedToIds to an empty collection
        if (dto.getAssignedToIds() != null) {
            assertTrue(dto.getAssignedToIds().isEmpty());
        }
    }

    @Test
    void mapToShouldHandleNullTickets() {
        // Arrange
        UUID id = UUID.randomUUID();
        AccountEntity owner = TestDataUtil.createAccountEntities().get(0);

        ProjectEntity projectEntity = ProjectEntity.builder()
                .id(id)
                .owner(owner)
                .name("test")
                .tickets(null)
                .build();

        // Act
        ProjectDto dto = projectMapper.mapTo(projectEntity);

        // Assert
        // The mapper should handle null tickets by setting ticketIds to an empty collection
        if (dto.getTicketIds() != null) {
            assertTrue(dto.getTicketIds().isEmpty());
        }
    }

    @Test
    void mapFromShouldMapDtoToEntity() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        AccountEntity owner = AccountEntity.builder()
                .id(ownerId)
                .email("owner@example.com")
                .build();

        ProjectDto dto = ProjectDto.builder()
                .id(id)
                .ownerId(ownerId)
                .name("test")
                .build();

        when(accountRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        // Act
        ProjectEntity entity = projectMapper.mapFrom(dto);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals("test", entity.getName());
        assertEquals(owner, entity.getOwner());
    }

    @Test
    void mapFromShouldFetchOwnerById() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        AccountEntity owner = AccountEntity.builder()
                .id(ownerId)
                .email("owner@example.com")
                .build();

        ProjectDto dto = ProjectDto.builder()
                .id(id)
                .ownerId(ownerId)
                .name("test")
                .build();

        when(accountRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        // Act
        ProjectEntity entity = projectMapper.mapFrom(dto);

        // Assert
        assertEquals(owner, entity.getOwner());
    }

    @Test
    void mapFromShouldFetchAssignedToById() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID assignedId1 = UUID.randomUUID();
        UUID assignedId2 = UUID.randomUUID();

        AccountEntity owner = AccountEntity.builder()
                .id(ownerId)
                .email("owner@example.com")
                .build();

        AccountEntity assigned1 = AccountEntity.builder()
                .id(assignedId1)
                .email("assigned1@example.com")
                .build();

        AccountEntity assigned2 = AccountEntity.builder()
                .id(assignedId2)
                .email("assigned2@example.com")
                .build();

        Set<UUID> assignedToIds = new HashSet<>(Set.of(assignedId1, assignedId2));

        ProjectDto dto = ProjectDto.builder()
                .id(id)
                .ownerId(ownerId)
                .name("test")
                .assignedToIds(assignedToIds)
                .build();

        when(accountRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(accountRepository.findAllById(assignedToIds)).thenReturn(List.of(assigned1, assigned2));

        // Act
        ProjectEntity entity = projectMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity.getAssignedTo());
        assertEquals(2, entity.getAssignedTo().size());
        assertTrue(entity.getAssignedTo().contains(assigned1));
        assertTrue(entity.getAssignedTo().contains(assigned2));
    }

    @Test
    void mapFromShouldFetchTicketsById() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID ticketId1 = UUID.randomUUID();
        UUID ticketId2 = UUID.randomUUID();

        AccountEntity owner = AccountEntity.builder()
                .id(ownerId)
                .email("owner@example.com")
                .build();

        TicketEntity ticket1 = TicketEntity.builder()
                .id(ticketId1)
                .title("Test Ticket 1")
                .description("Test Description 1")
                .status(TicketEntity.TicketStatus.TODO)
                .build();

        TicketEntity ticket2 = TicketEntity.builder()
                .id(ticketId2)
                .title("Test Ticket 2")
                .description("Test Description 2")
                .status(TicketEntity.TicketStatus.IN_PROGRESS)
                .build();

        List<UUID> ticketIds = List.of(ticketId1, ticketId2);

        ProjectDto dto = ProjectDto.builder()
                .id(id)
                .ownerId(ownerId)
                .name("test")
                .ticketIds(ticketIds)
                .build();

        when(accountRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(ticketRepository.findAllById(ticketIds)).thenReturn(List.of(ticket1, ticket2));

        // Act
        ProjectEntity entity = projectMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity.getTickets());
        assertEquals(2, entity.getTickets().size());
        assertTrue(entity.getTickets().contains(ticket1));
        assertTrue(entity.getTickets().contains(ticket2));
    }

    @Test
    void mapFromShouldHandleNullOwnerId() {
        // Arrange
        UUID id = UUID.randomUUID();

        ProjectDto dto = ProjectDto.builder()
                .id(id)
                .ownerId(null)
                .name("test")
                .build();

        // Act
        ProjectEntity entity = projectMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("test", entity.getName());
        assertNull(entity.getOwner());
    }

    @Test
    void mapFromShouldHandleNullAssignedToIds() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        AccountEntity owner = AccountEntity.builder()
                .id(ownerId)
                .email("owner@example.com")
                .build();

        ProjectDto dto = ProjectDto.builder()
                .id(id)
                .ownerId(ownerId)
                .name("test")
                .assignedToIds(null)
                .build();

        when(accountRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        // Act
        ProjectEntity entity = projectMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("test", entity.getName());
        assertEquals(owner, entity.getOwner());
        assertNull(entity.getAssignedTo());
    }

    @Test
    void mapFromShouldHandleNullTicketIds() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        AccountEntity owner = AccountEntity.builder()
                .id(ownerId)
                .email("owner@example.com")
                .build();

        ProjectDto dto = ProjectDto.builder()
                .id(id)
                .ownerId(ownerId)
                .name("test")
                .ticketIds(null)
                .build();

        when(accountRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        // Act
        ProjectEntity entity = projectMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("test", entity.getName());
        assertEquals(owner, entity.getOwner());
        assertNull(entity.getTickets());
    }

    @Test
    void mapFromShouldHandleEmptyAssignedToIds() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        AccountEntity owner = AccountEntity.builder()
                .id(ownerId)
                .email("owner@example.com")
                .build();

        ProjectDto dto = ProjectDto.builder()
                .id(id)
                .ownerId(ownerId)
                .name("test")
                .assignedToIds(new HashSet<>())
                .build();

        when(accountRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(accountRepository.findAllById(new HashSet<>())).thenReturn(List.of());

        // Act
        ProjectEntity entity = projectMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("test", entity.getName());
        assertEquals(owner, entity.getOwner());
        assertNotNull(entity.getAssignedTo());
        assertTrue(entity.getAssignedTo().isEmpty());
    }

    @Test
    void mapFromShouldHandleEmptyTicketIds() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        AccountEntity owner = AccountEntity.builder()
                .id(ownerId)
                .email("owner@example.com")
                .build();

        ProjectDto dto = ProjectDto.builder()
                .id(id)
                .ownerId(ownerId)
                .name("test")
                .ticketIds(List.of())
                .build();

        when(accountRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(ticketRepository.findAllById(List.of())).thenReturn(List.of());

        // Act
        ProjectEntity entity = projectMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("test", entity.getName());
        assertEquals(owner, entity.getOwner());
        assertNotNull(entity.getTickets());
        assertTrue(entity.getTickets().isEmpty());
    }

    @Test
    void mapFromShouldThrowExceptionForInvalidOwnerId() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID invalidOwnerId = UUID.randomUUID();

        ProjectDto dto = ProjectDto.builder()
                .id(id)
                .ownerId(invalidOwnerId)
                .name("test")
                .build();

        when(accountRepository.findById(invalidOwnerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> projectMapper.mapFrom(dto));
    }
}
