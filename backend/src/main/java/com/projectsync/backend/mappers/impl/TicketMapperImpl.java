package com.projectsync.backend.mappers.impl;

import com.projectsync.backend.domain.dto.TicketDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import com.projectsync.backend.mappers.Mapper;
import com.projectsync.backend.repositories.AccountRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class TicketMapperImpl implements Mapper<TicketEntity, TicketDto> {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    public TicketMapperImpl(ModelMapper modelMapper, AccountRepository accountRepository) {
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void init() {
        Converter<AccountEntity, UUID> accountToUuid = ctx -> ctx.getSource().getId();
        Converter<TicketEntity, UUID> ticketToUuid = ctx -> ctx.getSource().getId();

        modelMapper.createTypeMap(AccountEntity.class, UUID.class)
                .setConverter(accountToUuid);
        modelMapper.createTypeMap(TicketEntity.class, UUID.class)
                .setConverter(ticketToUuid);

        modelMapper.typeMap(TicketEntity.class, TicketDto.class).addMappings(mapper -> {
            mapper.map(src -> Optional.ofNullable(src.getAssignedTo())
                    .orElse(Set.of())
                    .stream().map(AccountEntity::getId), TicketDto::setAssignedToIds);
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
        // Note: The project entity is set in the TicketServiceImpl, not here
        // This is because we need to fetch the actual ProjectEntity from the database
        return ticketEntity;
    }
}
