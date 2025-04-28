package com.projectsync.backend.config;

import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.domain.entities.ProjectEntity;
import com.projectsync.backend.domain.entities.TicketEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Register global TypeMaps here (if you want)
        if (modelMapper.getTypeMap(ProjectEntity.class, UUID.class) == null) {
            modelMapper.createTypeMap(ProjectEntity.class, UUID.class)
                    .setConverter(ctx -> ctx.getSource().getId());
        }
        if (modelMapper.getTypeMap(AccountEntity.class, UUID.class) == null) {
            modelMapper.createTypeMap(AccountEntity.class, UUID.class)
                    .setConverter(ctx -> ctx.getSource().getId());
        }
        if (modelMapper.getTypeMap(TicketEntity.class, UUID.class) == null) {
            modelMapper.createTypeMap(TicketEntity.class, UUID.class)
                    .setConverter(ctx -> ctx.getSource().getId());
        }

        return modelMapper;
    }
}
