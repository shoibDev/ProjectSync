package com.projectsync.backend;

import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.domain.entities.TicketEntity;

import java.util.List;

public final class TestDataUtil {

    private TestDataUtil() {
    }

    public static List<AccountEntity> createAccountEntities(){
        return List.of(
                AccountEntity.builder()
                        .email("alice@example.com")
                        .build(),
                AccountEntity.builder()
                        .email("bob@example.com")
                        .build(),
                AccountEntity.builder()
                        .email("charlie@example.com")
                        .build(),
                AccountEntity.builder()
                        .email("diana@example.com")
                        .build(),
                AccountEntity.builder()
                        .email("edward@example.com")
                        .build()
        );
    }


    public static List<ProjectEntity> createProjectEntities() {
        List<AccountEntity> accounts = createAccountEntities();
        return List.of(
                ProjectEntity.builder()
                        .owner(accounts.get(0))
                        .name("Alpha")
                        .build(),
                ProjectEntity.builder()
                        .owner(accounts.get(1))
                        .name("Beta")
                        .build(),
                ProjectEntity.builder()
                        .owner(accounts.get(2))
                        .name("Gamma")
                        .build(),
                ProjectEntity.builder()
                        .owner(accounts.get(3))
                        .name("Delta")
                        .build(),
                ProjectEntity.builder()
                        .owner(accounts.get(4))
                        .name("Epsilon")
                        .build()
        );
    }

    public static List<TicketEntity> createTicketEntities() {
        List<ProjectEntity> projects = createProjectEntities();
        return List.of(
                TicketEntity.builder()
                        .title("Fix Login Bug")
                        .description("Users are unable to login with correct credentials")
                        .status(TicketEntity.TicketStatus.TODO)
                        .project(projects.get(0))
                        .build(),
                TicketEntity.builder()
                        .title("Implement User Dashboard")
                        .description("Create a dashboard showing user activity and statistics")
                        .status(TicketEntity.TicketStatus.IN_PROGRESS)
                        .project(projects.get(0))
                        .build(),
                TicketEntity.builder()
                        .title("Database Optimization")
                        .description("Optimize database queries for better performance")
                        .status(TicketEntity.TicketStatus.DONE)
                        .project(projects.get(1))
                        .build(),
                TicketEntity.builder()
                        .title("API Documentation")
                        .description("Create comprehensive documentation for the REST API")
                        .status(TicketEntity.TicketStatus.BLOCKED)
                        .project(projects.get(1))
                        .build(),
                TicketEntity.builder()
                        .title("Security Audit")
                        .description("Perform a security audit of the application")
                        .status(TicketEntity.TicketStatus.TODO)
                        .project(projects.get(2))
                        .build()
        );
    }
}
