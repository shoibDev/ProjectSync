package com.projectsync.backend.security.controller;

import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.security.dto.request.AccountLoginDto;
import com.projectsync.backend.security.dto.request.AccountRegisterDto;
import com.projectsync.backend.security.dto.response.LoginResponse;
import com.projectsync.backend.security.service.AuthenticationService;
import com.projectsync.backend.security.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AccountRegisterDto> register(@RequestBody AccountRegisterDto accountRegisterDto) {
        AccountEntity registeredUser = authenticationService.signup(accountRegisterDto);

        AccountRegisterDto response = AccountRegisterDto.builder()
                .email(registeredUser.getEmail())
                .password(registeredUser.getPassword())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody AccountLoginDto accountLoginDto) {
        AccountEntity authenticatedUser = authenticationService.authenticate(accountLoginDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }
}