package com.projectsync.backend.services.impl;

import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.mappers.impl.AccountMapperImpl;
import com.projectsync.backend.repositories.AccountRepository;
import com.projectsync.backend.services.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapperImpl accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapperImpl accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public List<AccountDto> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccountDto> findById(UUID id) {
        return accountRepository.findById(id)
                .map(accountMapper::mapTo);
    }

    @Override
    public Optional<AccountDto> findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .map(accountMapper::mapTo);
    }

    @Override
    public AccountDto save(AccountDto accountDto) {
        AccountEntity accountEntity = accountMapper.mapFrom(accountDto);
        AccountEntity savedEntity = accountRepository.save(accountEntity);
        return accountMapper.mapTo(savedEntity);
    }

    @Override
    public boolean existsById(UUID id) {
        return !accountRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        accountRepository.deleteById(id);
    }
}
