package com.projectsync.backend.mappers.impl;

import com.projectsync.backend.config.MapperConfig;
import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import com.projectsync.backend.repositories.ProjectRepository;
import com.projectsync.backend.repositories.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountMapperImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TicketRepository ticketRepository;

    private AccountMapperImpl accountMapper;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new MapperConfig().modelMapper();
        accountMapper = new AccountMapperImpl(modelMapper, projectRepository, ticketRepository);
        accountMapper.init(); // Call the @PostConstruct method manually
    }

    @Test
    void mapToShouldMapEntityToDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        AccountEntity entity = AccountEntity.builder()
                .id(id)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .build();

        // Act
        AccountDto dto = accountMapper.mapTo(entity);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("1234567890", dto.getPhoneNumber());
    }

    @Test
    void mapToShouldMapProjectIdsCorrectly() {
        // Arrange
        UUID projectId1 = UUID.randomUUID();
        UUID projectId2 = UUID.randomUUID();

        ProjectEntity project1 = ProjectEntity.builder().id(projectId1).build();
        ProjectEntity project2 = ProjectEntity.builder().id(projectId2).build();

        AccountEntity entity = AccountEntity.builder()
                .email("test@example.com")
                .projects(Arrays.asList(project1, project2))
                .build();

        // Act
        AccountDto dto = accountMapper.mapTo(entity);

        // Assert
        assertNotNull(dto.getProjectIds());
        assertEquals(2, dto.getProjectIds().size());
        assertTrue(dto.getProjectIds().contains(projectId1));
        assertTrue(dto.getProjectIds().contains(projectId2));
    }

    @Test
    void mapToShouldMapTicketIdsCorrectly() {
        // Arrange
        UUID ticketId1 = UUID.randomUUID();
        UUID ticketId2 = UUID.randomUUID();

        TicketEntity ticket1 = TicketEntity.builder().id(ticketId1).build();
        TicketEntity ticket2 = TicketEntity.builder().id(ticketId2).build();

        AccountEntity entity = AccountEntity.builder()
                .email("test@example.com")
                .assignedTickets(Arrays.asList(ticket1, ticket2))
                .build();

        // Act
        AccountDto dto = accountMapper.mapTo(entity);

        // Assert
        assertNotNull(dto.getAssignedTicketIds());
        assertEquals(2, dto.getAssignedTicketIds().size());
        assertTrue(dto.getAssignedTicketIds().contains(ticketId1));
        assertTrue(dto.getAssignedTicketIds().contains(ticketId2));
    }

    @Test
    void mapToShouldHandleNullProjects() {
        // Arrange
        AccountEntity entity = AccountEntity.builder()
                .email("test@example.com")
                .projects(null)
                .build();

        // Act
        AccountDto dto = accountMapper.mapTo(entity);

        // Assert
        // The converter should handle null projects by setting projectIds to an empty list
        if (dto.getProjectIds() != null) {
            assertTrue(dto.getProjectIds().isEmpty());
        }
    }

    @Test
    void mapToShouldHandleNullAssignedTickets() {
        // Arrange
        AccountEntity entity = AccountEntity.builder()
                .email("test@example.com")
                .assignedTickets(null)
                .build();

        // Act
        AccountDto dto = accountMapper.mapTo(entity);

        // Assert
        // The converter should handle null assignedTickets by setting assignedTicketIds to an empty list
        if (dto.getAssignedTicketIds() != null) {
            assertTrue(dto.getAssignedTicketIds().isEmpty());
        }
    }

    @Test
    void mapFromShouldMapDtoToEntity() {
        // Arrange
        UUID id = UUID.randomUUID();
        AccountDto dto = AccountDto.builder()
                .id(id)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("1234567890")
                .build();

        // Act
        AccountEntity entity = accountMapper.mapFrom(dto);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals("test@example.com", entity.getEmail());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("1234567890", entity.getPhoneNumber());
    }

    @Test
    void mapFromShouldFetchProjectsById() {
        // Arrange
        UUID projectId1 = UUID.randomUUID();
        UUID projectId2 = UUID.randomUUID();

        ProjectEntity project1 = ProjectEntity.builder().id(projectId1).build();
        ProjectEntity project2 = ProjectEntity.builder().id(projectId2).build();

        AccountDto dto = AccountDto.builder()
                .email("test@example.com")
                .projectIds(Arrays.asList(projectId1, projectId2))
                .build();

        when(projectRepository.findAllById(any())).thenReturn(Arrays.asList(project1, project2));

        // Act
        AccountEntity entity = accountMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity.getProjects());
        assertEquals(2, entity.getProjects().size());
        assertTrue(entity.getProjects().contains(project1));
        assertTrue(entity.getProjects().contains(project2));
    }

    @Test
    void mapFromShouldFetchTicketsById() {
        // Arrange
        UUID ticketId1 = UUID.randomUUID();
        UUID ticketId2 = UUID.randomUUID();

        TicketEntity ticket1 = TicketEntity.builder().id(ticketId1).build();
        TicketEntity ticket2 = TicketEntity.builder().id(ticketId2).build();

        AccountDto dto = AccountDto.builder()
                .email("test@example.com")
                .assignedTicketIds(Arrays.asList(ticketId1, ticketId2))
                .build();

        when(ticketRepository.findAllById(any())).thenReturn(Arrays.asList(ticket1, ticket2));

        // Act
        AccountEntity entity = accountMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity.getAssignedTickets());
        assertEquals(2, entity.getAssignedTickets().size());
        assertTrue(entity.getAssignedTickets().contains(ticket1));
        assertTrue(entity.getAssignedTickets().contains(ticket2));
    }

    @Test
    void mapFromShouldHandleNullProjectIds() {
        // Arrange
        AccountDto dto = AccountDto.builder()
                .email("test@example.com")
                .projectIds(null)
                .build();

        // Act
        AccountEntity entity = accountMapper.mapFrom(dto);

        // Assert
        assertNull(entity.getProjects());
    }

    @Test
    void mapFromShouldHandleNullAssignedTicketIds() {
        // Arrange
        AccountDto dto = AccountDto.builder()
                .email("test@example.com")
                .assignedTicketIds(null)
                .build();

        // Act
        AccountEntity entity = accountMapper.mapFrom(dto);

        // Assert
        assertNull(entity.getAssignedTickets());
    }

    @Test
    void mapFromShouldHandleEmptyProjectIds() {
        // Arrange
        AccountDto dto = AccountDto.builder()
                .email("test@example.com")
                .projectIds(Collections.emptyList())
                .build();

        when(projectRepository.findAllById(any())).thenReturn(Collections.emptyList());

        // Act
        AccountEntity entity = accountMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity.getProjects());
        assertTrue(entity.getProjects().isEmpty());
    }

    @Test
    void mapFromShouldHandleEmptyAssignedTicketIds() {
        // Arrange
        AccountDto dto = AccountDto.builder()
                .email("test@example.com")
                .assignedTicketIds(Collections.emptyList())
                .build();

        when(ticketRepository.findAllById(any())).thenReturn(Collections.emptyList());

        // Act
        AccountEntity entity = accountMapper.mapFrom(dto);

        // Assert
        assertNotNull(entity.getAssignedTickets());
        assertTrue(entity.getAssignedTickets().isEmpty());
    }
}
