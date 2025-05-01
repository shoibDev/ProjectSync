package com.projectsync.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.projectsync.backend.BaseIntegrationTest;
import com.projectsync.backend.TestDataUtil;
import com.projectsync.backend.controllers.AccountController;
import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.mappers.impl.AccountMapperImpl;
import com.projectsync.backend.security.dto.AccountLoginDto;
import com.projectsync.backend.security.dto.AccountRegisterDto;
import com.projectsync.backend.security.dto.LoginResponse;
import com.projectsync.backend.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class AccountControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AccountMapperImpl accountMapper;

    @Autowired
    private AccountService accountService;

    private List<AccountDto> registeredAccounts;
    private List<String> tokenList;

    @BeforeEach
    public void setup() throws Exception {
        registeredAccounts = new java.util.ArrayList<>();
        tokenList = new java.util.ArrayList<>();

        List<AccountEntity> accounts = TestDataUtil.createAccountEntities();
        for (AccountEntity account : accounts) {
            String token = registerAndLogin(account.getEmail(), account.getPassword());

            MvcResult accountResult = mockMvc.perform(MockMvcRequestBuilders.get("/account/email/" + account.getEmail())
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andReturn();

            String accountResponseContent = accountResult.getResponse().getContentAsString();
            AccountDto accountDto = objectMapper.readValue(accountResponseContent, AccountDto.class);
            registeredAccounts.add(accountDto);
            tokenList.add(token);
        }
    }

    @Test
    void testGetAllAccounts() throws Exception {
        // First register a user
        AccountEntity account = TestDataUtil.createAccountEntities().get(0);
        String token = login(account.getEmail(), account.getPassword());

        // Use the token to access the protected endpoint
        MvcResult accountList = mockMvc.perform(MockMvcRequestBuilders.get("/account")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String accountListResponseContent = accountList.getResponse().getContentAsString();

        // Deserialize JSON into List<AccountDto>
        JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, AccountDto.class);
        List<AccountDto> accountDtoList = objectMapper.readValue(accountListResponseContent, listType);

        assertFalse(accountDtoList.isEmpty(), "Account list should not be empty");

        accountDtoList.forEach(accountDto -> {
            Optional<AccountDto> match = accountDtoList.stream()
                    .filter(accountEntity -> accountEntity.getEmail().equals(accountDto.getEmail()))
                    .findFirst();

            assertTrue(match.isPresent(), "No matching AccountEntity found for email: " + accountDto.getEmail());

            assertNotNull(accountDto.getId(), "AccountDto ID should not be null");

            AccountDto matchedEntity = match.get();
            assertEquals(matchedEntity.getId(), accountDto.getId(), "ID mismatch for account: " + accountDto.getEmail());
        });
    }

    @Test
    void testGetAccountById() throws Exception {
        AccountDto account = registeredAccounts.get(0);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/account/" + account.getId())
                        .header("Authorization", "Bearer " + tokenList.get(0)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        AccountDto accountDto = objectMapper.readValue(responseContent, AccountDto.class);

        System.out.println(responseContent);

        assertNotNull(accountDto.getId(), "AccountDto ID should not be null");
        assertEquals(account.getId(), accountDto.getId(), "ID mismatch for account: " + account.getEmail());
    }

    @Test
    void testGetAccountByEmail() throws Exception {
        // First register a user

    }

    @Test
    void testCreateAccount() throws Exception {

    }

    @Test
    void testUpdateAccount() throws Exception {

    }

    @Test
    void testDeleteAccount() throws Exception {
    }
}
