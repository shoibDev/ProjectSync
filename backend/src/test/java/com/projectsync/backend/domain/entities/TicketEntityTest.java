package com.projectsync.backend.domain.entities;

import com.projectsync.backend.TestDataUtil;
import com.projectsync.backend.repositories.AccountRepository;
import com.projectsync.backend.repositories.ProjectRepository;
import com.projectsync.backend.repositories.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TicketEntityTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void itShouldSaveATicket() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());

        // Act
        ticketRepository.save(ticketEntity);

        // Assert
        assertTrue(ticketRepository.findById(ticketEntity.getId()).isPresent());
    }

    @Test
    void itShouldDeleteATicket() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());
        ticketRepository.save(ticketEntity);

        // Act
        ticketRepository.delete(ticketEntity);

        // Assert
        assertFalse(ticketRepository.findById(ticketEntity.getId()).isPresent());
    }

    @Test
    void itShouldFailWhenSavingTicketWithNullTitle() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());
        ticketEntity.setTitle(null); // Violation of the constraint

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> ticketRepository.saveAndFlush(ticketEntity));
    }

    @Test
    void itShouldFailWhenSavingTicketWithNullDescription() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());
        ticketEntity.setDescription(null); // Violation of the constraint

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> ticketRepository.saveAndFlush(ticketEntity));
    }

    @Test
    void itShouldFailWhenSavingTicketWithNullStatus() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());
        ticketEntity.setStatus(null); // Violation of the constraint

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> ticketRepository.saveAndFlush(ticketEntity));
    }

    @Test
    void itShouldFailWhenSavingTicketWithTitleExceeding100Characters() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());

        // Create a string with 101 characters
        String longTitle = "a".repeat(101);
        ticketEntity.setTitle(longTitle);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> ticketRepository.saveAndFlush(ticketEntity));
    }

    @Test
    void itShouldFailWhenSavingTicketWithDescriptionExceeding1000Characters() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());

        // Create a string with 1001 characters
        String longDescription = "a".repeat(1001);
        ticketEntity.setDescription(longDescription);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> ticketRepository.saveAndFlush(ticketEntity));
    }

    @Test
    void itShouldFailWhenSavingTicketWithDuplicateTitleInSameProject() {
        // Arrange
        TicketEntity ticketEntity1 = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity1.getProject().getOwner());
        projectRepository.save(ticketEntity1.getProject());
        ticketRepository.save(ticketEntity1);

        // Create second ticket with same title and project
        TicketEntity ticketEntity2 = TicketEntity.builder()
                .title(ticketEntity1.getTitle())
                .description("Different description")
                .status(TicketEntity.TicketStatus.TODO)
                .project(ticketEntity1.getProject())
                .build();

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> ticketRepository.saveAndFlush(ticketEntity2));
    }

    @Test
    void itShouldAllowSavingTicketsWithSameTitleInDifferentProjects() {
        // Arrange
        TicketEntity ticketEntity1 = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity1.getProject().getOwner());
        projectRepository.save(ticketEntity1.getProject());
        ticketRepository.save(ticketEntity1);

        // Create second ticket with same title but different project
        ProjectEntity differentProject = TestDataUtil.createProjectEntities().get(1);
        accountRepository.save(differentProject.getOwner());
        projectRepository.save(differentProject);

        TicketEntity ticketEntity2 = TicketEntity.builder()
                .title(ticketEntity1.getTitle())
                .description("Different description")
                .status(TicketEntity.TicketStatus.TODO)
                .project(differentProject)
                .build();

        // Act
        ticketRepository.save(ticketEntity2);

        // Assert
        assertTrue(ticketRepository.findById(ticketEntity2.getId()).isPresent());
    }

    @Test
    void itShouldUpdateTicketTitle() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());
        ticketRepository.save(ticketEntity);

        // Act
        String newTitle = "Updated Ticket Title";
        ticketEntity.setTitle(newTitle);
        ticketRepository.save(ticketEntity);

        // Assert
        TicketEntity updatedTicket = ticketRepository.findById(ticketEntity.getId()).orElseThrow();
        assertEquals(newTitle, updatedTicket.getTitle());
    }

    @Test
    void itShouldUpdateTicketStatus() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());
        ticketRepository.save(ticketEntity);

        // Act
        TicketEntity.TicketStatus newStatus = TicketEntity.TicketStatus.DONE;
        ticketEntity.setStatus(newStatus);
        ticketRepository.save(ticketEntity);

        // Assert
        TicketEntity updatedTicket = ticketRepository.findById(ticketEntity.getId()).orElseThrow();
        assertEquals(newStatus, updatedTicket.getStatus());
    }

    @Test
    void itShouldAssignUsersToTicket() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());

        // Create and save additional accounts
        AccountEntity account1 = TestDataUtil.createAccountEntities().get(1);
        AccountEntity account2 = TestDataUtil.createAccountEntities().get(2);
        accountRepository.save(account1);
        accountRepository.save(account2);

        // Act
        Set<AccountEntity> assignedUsers = new HashSet<>();
        assignedUsers.add(account1);
        assignedUsers.add(account2);
        ticketEntity.setAssignedTo(assignedUsers);

        ticketRepository.save(ticketEntity);

        // Assert
        TicketEntity savedTicket = ticketRepository.findById(ticketEntity.getId()).orElseThrow();
        assertEquals(2, savedTicket.getAssignedTo().size());
        assertTrue(savedTicket.getAssignedTo().contains(account1));
        assertTrue(savedTicket.getAssignedTo().contains(account2));
    }

    @Test
    void itShouldChangeTicketProject() {
        // Arrange
        TicketEntity ticketEntity = TestDataUtil.createTicketEntities().get(0);
        accountRepository.save(ticketEntity.getProject().getOwner());
        projectRepository.save(ticketEntity.getProject());
        ticketRepository.save(ticketEntity);

        // Create and save a different project
        ProjectEntity newProject = TestDataUtil.createProjectEntities().get(1);
        accountRepository.save(newProject.getOwner());
        projectRepository.save(newProject);

        // Act
        ticketEntity.setProject(newProject);
        ticketRepository.save(ticketEntity);

        // Assert
        TicketEntity updatedTicket = ticketRepository.findById(ticketEntity.getId()).orElseThrow();
        assertEquals(newProject.getId(), updatedTicket.getProject().getId());
    }
}
