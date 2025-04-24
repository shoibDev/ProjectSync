package com.projectsync.backend.security.controller;

import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.security.dto.AccountLoginDto;
import com.projectsync.backend.security.dto.AccountRegisterDto;
import com.projectsync.backend.security.service.AuthenticationService;
import com.projectsync.backend.security.service.JwtService;
import lombok.Builder;
import lombok.Data;
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
    public ResponseEntity<AccountEntity> register(@RequestBody AccountRegisterDto accountRegisterDto) {
        AccountEntity registeredUser = authenticationService.signup(accountRegisterDto);

        return ResponseEntity.ok(registeredUser);
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

@Data
@Builder
class LoginResponse {
    private String token;
    private long expiresIn;
}