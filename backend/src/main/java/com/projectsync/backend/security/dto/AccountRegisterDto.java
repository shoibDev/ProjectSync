package com.projectsync.backend.security.dto;

import lombok.Data;

@Data
public class AccountRegisterDto {
    private String email;
    private String password;
}
