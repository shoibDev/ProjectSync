package com.projectsync.backend.mappers.impl;

import com.projectsync.backend.domain.dto.ProjectDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import com.projectsync.backend.mappers.Mapper;
import com.projectsync.backend.repositories.AccountRepository;
import com.projectsync.backend.repositories.TicketRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ProjectMapperImpl implements Mapper<ProjectEntity, ProjectDto> {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;
    private final TicketRepository ticketRepository;

    public ProjectMapperImpl(ModelMapper modelMapper, AccountRepository accountRepository, TicketRepository ticketRepository) {
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostConstruct
    public void init() {
        Converter<AccountEntity, UUID> accountToUuid = ctx -> ctx.getSource().getId();
        Converter<TicketEntity, UUID> ticketToUuid = ctx -> ctx.getSource().getId();

        modelMapper.createTypeMap(AccountEntity.class, UUID.class)
                .setConverter(accountToUuid);
        modelMapper.createTypeMap(TicketEntity.class, UUID.class)
                .setConverter(ticketToUuid);

        modelMapper.typeMap(ProjectEntity.class, ProjectDto.class)
                .addMappings(mapper -> {
                    mapper.map(
                            src -> Optional.ofNullable(src.getAssignedTo())
                                    .orElse(Set.of())
                                    .stream().map(AccountEntity::getId).toList(),
                            ProjectDto::setAssignedToIds
                    );
                    mapper.map(
                            src -> Optional.ofNullable(src.getTickets())
                                    .orElse(List.of())
                                    .stream().map(TicketEntity::getId).toList(),
                            ProjectDto::setTicketIds
                    );
                    mapper.map(src -> src.getOwner().getId(), ProjectDto::setOwnerId);
                });
    }

    @Override
    public ProjectDto mapTo(ProjectEntity projectEntity) {
        return modelMapper.map(projectEntity, ProjectDto.class);
    }

    @Override
    public ProjectEntity mapFrom(ProjectDto projectDto) {
        ProjectEntity projectEntity = modelMapper.map(projectDto, ProjectEntity.class);

        if (projectDto.getAssignedToIds() != null) {
            Set<AccountEntity> assignedTo = StreamSupport
                    .stream(accountRepository.findAllById(projectDto.getAssignedToIds()).spliterator(), false)
                    .collect(Collectors.toSet());
            projectEntity.setAssignedTo(assignedTo);
        }
        if (projectDto.getTicketIds() != null) {
            List<TicketEntity> tickets = StreamSupport
                    .stream(ticketRepository.findAllById(projectDto.getTicketIds()).spliterator(), false)
                    .toList();
            projectEntity.setTickets(tickets);
        }

        if (projectDto.getOwnerId() != null) {
            AccountEntity owner = accountRepository.findById(projectDto.getOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid owner ID: " + projectDto.getOwnerId()));
            projectEntity.setOwner(owner);
        }

        return projectEntity;
    }
}
