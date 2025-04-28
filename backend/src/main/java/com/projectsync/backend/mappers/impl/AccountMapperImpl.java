package com.projectsync.backend.mappers.impl;

import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import com.projectsync.backend.mappers.Mapper;
import com.projectsync.backend.repositories.ProjectRepository;
import com.projectsync.backend.repositories.TicketRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.SourceGetter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class AccountMapperImpl implements Mapper<AccountEntity, AccountDto> {

    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;

    public AccountMapperImpl(ModelMapper modelMapper, ProjectRepository projectRepository, TicketRepository ticketRepository) {
        this.modelMapper = modelMapper;
        this.projectRepository = projectRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostConstruct
    public void init() {
        if (modelMapper.getTypeMap(AccountEntity.class, AccountDto.class) == null) {
            modelMapper.typeMap(AccountEntity.class, AccountDto.class)
                    .addMappings(mapper -> {
                        mapper.map(
                                src -> Optional.ofNullable(src.getProjects())
                                        .orElse(List.of())
                                        .stream().map(ProjectEntity::getId).toList(),
                                AccountDto::setProjectIds
                        );
                        mapper.map(
                                src -> Optional.ofNullable(src.getAssignedTickets())
                                        .orElse(List.of())
                                        .stream().map(TicketEntity::getId).toList(),
                                AccountDto::setAssignedTicketIds
                        );
                    });
        }
    }

    @Override
    public AccountDto mapTo(AccountEntity accountEntity) {
        return modelMapper.map(accountEntity, AccountDto.class);
    }

    @Override
    public AccountEntity mapFrom(AccountDto accountDto) {
        AccountEntity accountEntity = modelMapper.map(accountDto, AccountEntity.class);

        if (accountDto.getProjectIds() != null) {
            List<ProjectEntity> projects = StreamSupport
                    .stream(projectRepository.findAllById(accountDto.getProjectIds()).spliterator(), false)
                    .collect(Collectors.toList());
            accountEntity.setProjects(projects);
        }
        if (accountDto.getAssignedTicketIds() != null) {
            List<TicketEntity> tickets = StreamSupport
                    .stream(ticketRepository.findAllById(accountDto.getAssignedTicketIds()).spliterator(), false)
                    .collect(Collectors.toList());
            accountEntity.setAssignedTickets(tickets);
        }

        return accountEntity;
    }
}
