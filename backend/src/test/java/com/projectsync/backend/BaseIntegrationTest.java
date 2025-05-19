package com.projectsync.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectsync.backend.security.dto.request.AccountLoginDto;
import com.projectsync.backend.security.dto.request.AccountRegisterDto;
import com.projectsync.backend.security.dto.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String login(String email, String password) throws Exception {
        // Login
        AccountLoginDto loginDto = new AccountLoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract and return token
        String response = result.getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(response, LoginResponse.class);
        return loginResponse.getToken();

    }

    protected String registerAndLogin(String email, String password) throws Exception {
        // Register
        AccountRegisterDto registerDto = AccountRegisterDto.builder()
                .email(email)
                .password(password)
                .build();

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());

        // Login
        AccountLoginDto loginDto = new AccountLoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract and return token
        String response = result.getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(response, LoginResponse.class);
        return loginResponse.getToken();
    }

}
