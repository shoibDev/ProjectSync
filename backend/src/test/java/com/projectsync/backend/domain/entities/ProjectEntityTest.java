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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProjectEntityTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void itShouldSaveAProject() {
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        accountRepository.save(projectEntity.getOwner());
        projectRepository.save(projectEntity);
        assert projectRepository.findById(projectEntity.getId()).isPresent();
    }

    @Test
    void itShouldDeleteAProject() {
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        accountRepository.save(projectEntity.getOwner());
        projectRepository.save(projectEntity);
        projectRepository.delete(projectEntity);
        assert projectRepository.findById(projectEntity.getId()).isEmpty();
    }

    @Test
    void itShouldFailWhenSavingProjectWithNullOwner(){
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        accountRepository.save(projectEntity.getOwner());
        projectEntity.setOwner(null); // Violation of the constraint

        assertThrows(DataIntegrityViolationException.class, () -> projectRepository.saveAndFlush(projectEntity));
    }

    @Test
    void itShouldFailWhenSavingProjectWithNullName() {
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        accountRepository.save(projectEntity.getOwner());
        projectEntity.setName(null); // Violation of the constraint

        assertThrows(DataIntegrityViolationException.class, () -> projectRepository.saveAndFlush(projectEntity));
    }

    @Test
    void itShouldFailWhenSavingProjectWithNameExceeding100Characters() {
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        accountRepository.save(projectEntity.getOwner());

        // Create a string with 101 characters
        String longName = "a".repeat(101);
        projectEntity.setName(longName);

        assertThrows(DataIntegrityViolationException.class, () -> projectRepository.saveAndFlush(projectEntity));
    }

    @Test
    void itShouldFailWhenSavingProjectWithDuplicateNameAndOwner() {
        // Save first project
        ProjectEntity projectEntity1 = TestDataUtil.createProjectEntities().get(0);
        accountRepository.save(projectEntity1.getOwner());
        projectRepository.save(projectEntity1);

        // Create the second project with the same name and owner
        ProjectEntity projectEntity2 = ProjectEntity.builder()
                .owner(projectEntity1.getOwner())
                .name(projectEntity1.getName())
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> projectRepository.saveAndFlush(projectEntity2));
    }

    @Test
    void itShouldAllowSavingProjectsWithSameNameButDifferentOwners() {
        // Save first project
        ProjectEntity projectEntity1 = TestDataUtil.createProjectEntities().get(0);
        accountRepository.save(projectEntity1.getOwner());
        projectRepository.save(projectEntity1);

        // Create the second project with the same name but different owner
        AccountEntity differentOwner = TestDataUtil.createAccountEntities().get(1);
        accountRepository.save(differentOwner);

        ProjectEntity projectEntity2 = ProjectEntity.builder()
                .owner(differentOwner)
                .name(projectEntity1.getName())
                .build();

        projectRepository.save(projectEntity2);

        assertTrue(projectRepository.findById(projectEntity2.getId()).isPresent());
    }

    @Test
    void itShouldUpdateProjectName() {
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        accountRepository.save(projectEntity.getOwner());
        projectRepository.save(projectEntity);

        String newName = "Updated Project Name";
        projectEntity.setName(newName);
        projectRepository.save(projectEntity);

        ProjectEntity updatedProject = projectRepository.findById(projectEntity.getId()).orElseThrow();
        assertEquals(newName, updatedProject.getName());
    }

    @Test
    void itShouldAssignUsersToProject() {
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        accountRepository.save(projectEntity.getOwner());

        // Create and save additional accounts
        List<AccountEntity> accounts = TestDataUtil.createAccountEntities();
        AccountEntity account1 = accounts.get(1);
        AccountEntity account2 = accounts.get(2);
        accountRepository.save(account1);
        accountRepository.save(account2);

        // Assign users to a project
        Set<AccountEntity> assignedUsers = new HashSet<>();
        assignedUsers.add(account1);
        assignedUsers.add(account2);
        projectEntity.setAssignedTo(assignedUsers);

        projectRepository.save(projectEntity);

        ProjectEntity savedProject = projectRepository.findById(projectEntity.getId()).orElseThrow();
        assertEquals(2, savedProject.getAssignedTo().size());
        assertTrue(savedProject.getAssignedTo().contains(account1));
        assertTrue(savedProject.getAssignedTo().contains(account2));
    }

    @Test
    void itShouldCascadeDeleteTicketsWhenProjectIsDeleted() {
        // Create and save a project
        ProjectEntity projectEntity = TestDataUtil.createProjectEntities().get(0);
        accountRepository.save(projectEntity.getOwner());

        // Create and save ticket associated with the project
        TicketEntity ticket = TicketEntity.builder()
                .title("Test Ticket")
                .description("Test Description")
                .status(TicketEntity.TicketStatus.TODO)
                .project(projectEntity)
                .build();

        ticketRepository.save(ticket);

        projectEntity.setTickets(List.of(ticket));
        ticket.setProject(projectEntity);
        projectRepository.save(projectEntity);

        // Verify ticket exists
        assertNotNull(ticket.getId());
        assertTrue(ticketRepository.findById(ticket.getId()).isPresent());

        // Delete project
        projectRepository.delete(projectEntity);

        // Verify ticket was deleted
        assertFalse(ticketRepository.findById(ticket.getId()).isPresent());
    }
}
