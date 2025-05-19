package com.projectsync.backend.security.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRegisterDto {
    private String email;
    private String password;
}
