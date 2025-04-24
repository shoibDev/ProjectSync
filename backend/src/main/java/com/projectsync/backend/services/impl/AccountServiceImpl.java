package com.projectsync.backend.services.impl;

import com.projectsync.backend.mappers.impl.AccountMapperImpl;
import com.projectsync.backend.repositories.AccountRepository;
import com.projectsync.backend.services.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapperImpl accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapperImpl accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

}
