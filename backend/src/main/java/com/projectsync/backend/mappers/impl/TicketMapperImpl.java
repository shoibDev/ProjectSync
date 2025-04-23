package com.projectsync.backend.mappers.impl;

import com.projectsync.backend.domain.dto.TicketDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import com.projectsync.backend.mappers.Mapper;
import com.projectsync.backend.repositories.AccountRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TicketMapperImpl implements Mapper<TicketEntity, TicketDto> {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    public TicketMapperImpl(ModelMapper modelMapper, AccountRepository accountRepository) {
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void init() {
        modelMapper.typeMap(TicketEntity.class, TicketDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getAssignedTo().stream().map(AccountEntity::getId), TicketDto::setAssignedToIds);
            mapper.map(src -> src.getProject().getId(), TicketDto::setProjectId);
        });
    }

    @Override
    public TicketDto mapTo(TicketEntity ticketEntity) {
        return modelMapper.map(ticketEntity, TicketDto.class);
    }

    @Override
    public TicketEntity mapFrom(TicketDto ticketDto) {
        TicketEntity ticketEntity = modelMapper.map(ticketDto, TicketEntity.class);

        if (ticketDto.getAssignedToIds() != null) {
            Set<AccountEntity> assignedTo = StreamSupport
                    .stream(accountRepository.findAllById(ticketDto.getAssignedToIds()).spliterator(), false)
                    .collect(Collectors.toSet());
            ticketEntity.setAssignedTo(assignedTo);
        }
        if (ticketDto.getProjectId() != null) {
            TicketEntity project = new TicketEntity();
            project.setId(ticketDto.getProjectId());
        }
        return ticketEntity;
    }
}
