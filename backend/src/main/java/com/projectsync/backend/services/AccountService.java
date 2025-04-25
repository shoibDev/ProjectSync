package com.projectsync.backend.services;

import com.projectsync.backend.domain.dto.AccountDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    List<AccountDto> findAll();

    Optional<AccountDto> findById(UUID id);

    Optional<AccountDto> findByEmail(String email);

    AccountDto save(AccountDto accountDto);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
