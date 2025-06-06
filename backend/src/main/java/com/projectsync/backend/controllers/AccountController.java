package com.projectsync.backend.controllers;

import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.mappers.impl.AccountMapperImpl;
import com.projectsync.backend.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapperImpl accountMapper;

    public AccountController(AccountService accountService, AccountMapperImpl accountMapper){
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountService.findAll();
        System.out.println("Retrieved accounts: " + accounts);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable UUID id) {
        Optional<AccountDto> account = accountService.findById(id);
        return account.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AccountDto> getAccountByEmail(@PathVariable String email) {
        Optional<AccountDto> account = accountService.findByEmail(email);
        return account.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        AccountDto createdAccount = accountService.save(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable UUID id, @RequestBody AccountDto accountDto) {
        if (!accountService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        accountDto.setId(id);
        AccountDto updatedAccount = accountService.save(accountDto);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        if (!accountService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
