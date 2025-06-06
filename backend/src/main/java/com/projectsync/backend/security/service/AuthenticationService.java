package com.projectsync.backend.security.service;

import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.repositories.AccountRepository;
import com.projectsync.backend.security.dto.request.AccountLoginDto;
import com.projectsync.backend.security.dto.request.AccountRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AccountEntity signup(AccountRegisterDto input) {
        String verificationToken = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(15);

        AccountEntity account = AccountEntity.builder()
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .enabled(true)
//                 .verificationToken(verificationToken)
//                 .verificationTokenExpiry(expirationTime)
                .verificationToken(null)
                .verificationTokenExpiry(null)
                .build();

        AccountEntity savedAccount = accountRepository.save(account);
//         emailService.sendVerificationEmail(savedAccount.getEmail(), verificationToken);
        return savedAccount;
    }

    public AccountEntity authenticate(AccountLoginDto input) {
        AccountEntity account = accountRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!account.isEnabled()) {
            throw new IllegalStateException("Please verify your email before logging in.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return account;
    }

    public boolean verifyAccount(String token) {
        Optional<AccountEntity> optionalAccount = accountRepository.findByVerificationToken(token);

        if (optionalAccount.isEmpty()) return false;

        AccountEntity account = optionalAccount.get();

        if (account.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) return false;

        account.setEnabled(true);
        account.setVerificationToken(null);
        account.setVerificationTokenExpiry(null);

        accountRepository.save(account);

        return true;
    }
}