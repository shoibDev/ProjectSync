package com.projectsync.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectsync.backend.TestDataUtil;
import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.mappers.impl.AccountMapperImpl;
import com.projectsync.backend.services.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AccountMapperImpl accountMapper;
    private final AccountService accountService;

    @Autowired
    public AccountControllerIntegrationTest(
            MockMvc mockMvc,
            AccountMapperImpl accountMapper, AccountService accountService
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.accountMapper = accountMapper;
        this.accountService = accountService;
    }

    @Test
    void testGetAllAccounts() throws Exception {
        // Test for GET /accounts
    }

    @Test
    void testGetAccountById() throws Exception {

    }

    @Test
    void testGetAccountByEmail() {
        // Test for GET /accounts/email/{email}
    }

    @Test
    void testCreateAccount() {
        // Test for POST /accounts
    }

    @Test
    void testUpdateAccount() {
        // Test for PUT /accounts/{id}
    }

    @Test
    void testDeleteAccount() {
        // Test for DELETE /accounts/{id}
    }
}
