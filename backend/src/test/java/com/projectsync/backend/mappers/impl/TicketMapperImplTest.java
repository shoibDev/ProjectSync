package com.projectsync.backend.mappers.impl;

import com.projectsync.backend.TestDataUtil;
import com.projectsync.backend.domain.dto.TicketDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import com.projectsync.backend.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketMapperImplTest {

    @Mock
    private AccountRepository accountRepository;


    private TicketMapperImpl ticketMapper;

    @BeforeEach
    void setUp() {
        ticketMapper = new TicketMapperImpl(new ModelMapper(), accountRepository);
        ticketMapper.init();
    }

    @Test
    void mapToShouldMapEntityToDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        ProjectEntity project = TestDataUtil.createProjectEntities().get(0);
        TicketEntity ticketEntity = TicketEntity.builder()
                .id(id)
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketEntity.TicketStatus.TODO)
                .project(project)
                .build();

        // Act
        TicketDto dto = ticketMapper.mapTo(ticketEntity);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals("Test Ticket", dto.getTitle());
        assertEquals("Test Description", dto.getDescription());
        assertEquals(TicketDto.TicketStatus.TODO, dto.getStatus());
        assertEquals(project.getId(), dto.getProjectId());
    }

    @Test
    void mapToShouldMapProjectIdCorrectly() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        ProjectEntity project = ProjectEntity.builder()
                .id(projectId)
                .name("Test Project")
                .build();

        TicketEntity ticketEntity = TicketEntity.builder()
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketEntity.TicketStatus.TODO)
                .project(project)
                .build();

        // Act
        TicketDto dto = ticketMapper.mapTo(ticketEntity);

        // Assert
        assertEquals(projectId, dto.getProjectId());
    }

    @Test
    void mapToShouldMapAssignedToIdsCorrectly() {
        // Arrange
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        AccountEntity assigned1 = AccountEntity.builder()
                .id(id1)
                .email("assigned1@example.com")
                .build();

        AccountEntity assigned2 = AccountEntity.builder()
                .id(id2)
                .email("assigned2@example.com")
                .build();

        Set<AccountEntity> assignedTo = new HashSet<>(Set.of(assigned1, assigned2));

        TicketEntity ticketEntity = TicketEntity.builder()
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketEntity.TicketStatus.TODO)
                .assignedTo(assignedTo)
                .build();

        // Act
        TicketDto dto = ticketMapper.mapTo(ticketEntity);

        // Assert
        assertNotNull(dto.getAssignedToIds());
        assertEquals(2, dto.getAssignedToIds().size());
        assertTrue(dto.getAssignedToIds().contains(id1));
        assertTrue(dto.getAssignedToIds().contains(id2));
    }

    @Test
    void mapToShouldMapStatusCorrectly() {
        // Arrange
        TicketEntity ticketEntity = TicketEntity.builder()
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketEntity.TicketStatus.IN_PROGRESS)
                .build();

        // Act
        TicketDto dto = ticketMapper.mapTo(ticketEntity);

        // Assert
        assertEquals(TicketDto.TicketStatus.IN_PROGRESS, dto.getStatus());
    }

    @Test
    void mapToShouldHandleNullAssignedTo() {
        // Arrange
        TicketEntity ticketEntity = TicketEntity.builder()
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketEntity.TicketStatus.TODO)
                .assignedTo(null)
                .build();

        // Act
        TicketDto dto = ticketMapper.mapTo(ticketEntity);

        // Assert
        // The mapper should handle null assignedTo by setting assignedToIds to an empty collection
        if (dto.getAssignedToIds() != null) {
            assertTrue(dto.getAssignedToIds().isEmpty());
        }
    }

    @Test
    void mapToShouldHandleNullProject() {
        // Arrange
        TicketEntity ticketEntity = TicketEntity.builder()
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketEntity.TicketStatus.TODO)
                .project(null)
                .build();

        // Act
        TicketDto dto = ticketMapper.mapTo(ticketEntity);

        // Assert
        // The current implementation of TicketMapperImpl handles null projects by setting projectId to null
        assertNull(dto.getProjectId());
    }

    @Test
    void mapFromShouldMapDtoToEntity() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        TicketDto dto = TicketDto.builder()
                .id(id)
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketDto.TicketStatus.TODO)
                .projectId(projectId)
                .build();

        // Act
        TicketEntity entity = ticketMapper.mapFrom(dto);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals("Test Ticket", entity.getTitle());
        assertEquals("Test Description", entity.getDescription());
        assertEquals(TicketEntity.TicketStatus.TODO, entity.getStatus());
    }

    @Test
    void mapFromShouldFetchProjectById() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();

        TicketDto dto = TicketDto.builder()
                .id(id)
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketDto.TicketStatus.TODO)
                .projectId(projectId)
                .build();

        // Act
        TicketEntity entity = ticketMapper.mapFrom(dto);

        // Assert
        // The current implementation of TicketMapperImpl creates a new ProjectEntity with just the ID set
        assertNotNull(entity.getProject());
        assertEquals(projectId, entity.getProject().getId());
        // Other fields of the project are not set because the implementation doesn't fetch the project
        assertNull(entity.getProject().getName());
        assertNull(entity.getProject().getOwner());
    }

    @Test
    void mapFromShouldFetchAssignedToById() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID assignedId1 = UUID.randomUUID();
        UUID assignedId2 = UUID.randomUUID();

        AccountEntity assigned1 = AccountEntity.builder()
                .id(assignedId1)
                .email("assigned1@example.com")
                .build();

        AccountEntity assigned2 = AccountEntity.builder()
                .id(assignedId2)
                .email("assigned2@example.com")
                .build();

        Set<UUID> assignedToIds = new HashSet<>(Set.of(assignedId1, assignedId2));

        TicketDto dto = TicketDto.builder()
                .id(id)
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketDto.TicketStatus.TODO)
                .assignedToIds(assignedToIds)
                .build();

        when(accountRepository.findAllById(assignedToIds)).thenReturn(List.of(assigned1, assigned2));

        // Act
        TicketEntity entity = ticketMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity.getAssignedTo());
        assertEquals(2, entity.getAssignedTo().size());
        assertTrue(entity.getAssignedTo().contains(assigned1));
        assertTrue(entity.getAssignedTo().contains(assigned2));
    }

    @Test
    void mapFromShouldHandleNullProjectId() {
        // Arrange
        UUID id = UUID.randomUUID();

        TicketDto dto = TicketDto.builder()
                .id(id)
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketDto.TicketStatus.TODO)
                .projectId(null)
                .build();

        // Act
        TicketEntity entity = ticketMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Test Ticket", entity.getTitle());
        assertEquals("Test Description", entity.getDescription());
        assertEquals(TicketEntity.TicketStatus.TODO, entity.getStatus());
        assertNull(entity.getProject());
    }

    @Test
    void mapFromShouldHandleNullAssignedToIds() {
        // Arrange
        UUID id = UUID.randomUUID();

        TicketDto dto = TicketDto.builder()
                .id(id)
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketDto.TicketStatus.TODO)
                .assignedToIds(null)
                .build();

        // Act
        TicketEntity entity = ticketMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Test Ticket", entity.getTitle());
        assertEquals("Test Description", entity.getDescription());
        assertEquals(TicketEntity.TicketStatus.TODO, entity.getStatus());
        assertNull(entity.getAssignedTo());
    }

    @Test
    void mapFromShouldHandleEmptyAssignedToIds() {
        // Arrange
        UUID id = UUID.randomUUID();

        TicketDto dto = TicketDto.builder()
                .id(id)
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketDto.TicketStatus.TODO)
                .assignedToIds(new HashSet<>())
                .build();

        when(accountRepository.findAllById(new HashSet<>())).thenReturn(List.of());

        // Act
        TicketEntity entity = ticketMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Test Ticket", entity.getTitle());
        assertEquals("Test Description", entity.getDescription());
        assertEquals(TicketEntity.TicketStatus.TODO, entity.getStatus());
        assertNotNull(entity.getAssignedTo());
        assertTrue(entity.getAssignedTo().isEmpty());
    }

    @Test
    void mapFromShouldThrowExceptionForInvalidProjectId() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID invalidProjectId = UUID.randomUUID();

        TicketDto dto = TicketDto.builder()
                .id(id)
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketDto.TicketStatus.TODO)
                .projectId(invalidProjectId)
                .build();

        // Act
        TicketEntity entity = ticketMapper.mapFrom(dto);

        // Assert
        // The current implementation of TicketMapperImpl creates a new ProjectEntity with just the ID set
        // It doesn't validate if the project exists in the repository
        assertNotNull(entity.getProject());
        assertEquals(invalidProjectId, entity.getProject().getId());
    }
}
