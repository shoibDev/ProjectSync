package com.projectsync.backend.repositories;

import com.projectsync.backend.domain.entities.ProjectEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends CrudRepository<ProjectEntity, UUID> {
}
