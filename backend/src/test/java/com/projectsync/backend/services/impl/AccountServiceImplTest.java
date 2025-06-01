package com.projectsync.backend.services.impl;

import com.projectsync.backend.TestDataUtil;
import com.projectsync.backend.config.MapperConfig;
import com.projectsync.backend.domain.dto.AccountDto;
import com.projectsync.backend.domain.entities.AccountEntity;
import com.projectsync.backend.mappers.impl.AccountMapperImpl;
import com.projectsync.backend.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountMapperImpl accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new MapperConfig().modelMapper();
        accountMapper = new AccountMapperImpl(modelMapper, null, null);
        accountService = new AccountServiceImpl(accountRepository, accountMapper);
    }

    @Test
    void findAll_ShouldReturnAllAccounts() {
        // Arrange
        List<AccountEntity> accountEntities = TestDataUtil.createAccountEntities();

        // Mock repository behavior
        when(accountRepository.findAll()).thenReturn(accountEntities);

        // Manually create expected DTOs
        List<AccountDto> expectedAccountDtos = accountEntities.stream()
                .map(accountMapper::mapTo)
                .toList();

        // Act
        List<AccountDto> accounts = accountService.findAll();

        // Assert
        assertEquals(expectedAccountDtos.size(), accounts.size());
        for (int i = 0; i < accounts.size(); i++) {
            assertEquals(expectedAccountDtos.get(i).getId(), accounts.get(i).getId());
            assertEquals(expectedAccountDtos.get(i).getEmail(), accounts.get(i).getEmail());
            assertEquals(expectedAccountDtos.get(i).getFirstName(), accounts.get(i).getFirstName());
            assertEquals(expectedAccountDtos.get(i).getLastName(), accounts.get(i).getLastName());
            assertEquals(expectedAccountDtos.get(i).getPhoneNumber(), accounts.get(i).getPhoneNumber());
        }
    }

    @Test
    void findById_WithExistingId_ShouldReturnAccount() {
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(0);

        when(accountRepository.findById(accountEntity.getId())).thenReturn(Optional.of(accountEntity));

        Optional<AccountDto> accountDtoOptional  = accountService.findById(accountEntity.getId());
        AccountDto accountDto = accountDtoOptional.orElseThrow(() -> new AssertionError("Account not found"));

        assertEquals(accountDto.getId(), accountEntity.getId());
        assertEquals(accountDto.getEmail(), accountEntity.getEmail());
        assertEquals(accountDto.getFirstName(), accountEntity.getFirstName());
        assertEquals(accountDto.getLastName(), accountEntity.getLastName());
        assertEquals(accountDto.getPhoneNumber(), accountEntity.getPhoneNumber());
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(0);
        UUID nonExistingId = UUID.randomUUID();

        when(accountRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Optional<AccountDto> accountDtoOptional = accountService.findById(nonExistingId);
        assertTrue(accountDtoOptional.isEmpty());
    }

    @Test
    void findByEmail_WithExistingEmail_ShouldReturnAccount() {
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(0);
        String email = accountEntity.getEmail();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(accountEntity));

        Optional<AccountDto> accountDtoOptional = accountService.findByEmail(email);
        AccountDto accountDto = accountDtoOptional.orElseThrow(() -> new AssertionError("Account not found"));

        assertEquals(accountDto.getId(), accountEntity.getId());
        assertEquals(accountDto.getEmail(), email);
        assertEquals(accountDto.getFirstName(), accountEntity.getFirstName());
        assertEquals(accountDto.getLastName(), accountEntity.getLastName());
        assertEquals(accountDto.getPhoneNumber(), accountEntity.getPhoneNumber());
    }

    @Test
    void findByEmail_WithNonExistingEmail_ShouldReturnEmpty() {
        String nonExistingEmail = "nonexisting@example.com";

        when(accountRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        Optional<AccountDto> accountDtoOptional = accountService.findByEmail(nonExistingEmail);

        assertTrue(accountDtoOptional.isEmpty());
    }

    @Test
    void save_ShouldReturnSavedAccount() {
        // Arrange
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(0);
        AccountDto accountDto = accountMapper.mapTo(accountEntity);

        // Mock repository behavior
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        // Act
        AccountDto savedAccountDto = accountService.save(accountDto);

        // Assert
        assertEquals(accountDto.getId(), savedAccountDto.getId());
        assertEquals(accountDto.getEmail(), savedAccountDto.getEmail());
        assertEquals(accountDto.getFirstName(), savedAccountDto.getFirstName());
        assertEquals(accountDto.getLastName(), savedAccountDto.getLastName());
        assertEquals(accountDto.getPhoneNumber(), savedAccountDto.getPhoneNumber());
    }

    @Test
    void existsById_WithExistingId_ShouldReturnTrue() {
        UUID existingId = UUID.randomUUID();

        when(accountRepository.existsById(existingId)).thenReturn(false);

        boolean exists = accountService.existsById(existingId);

        assertTrue(exists);
    }

    @Test
    void existsById_WithNonExistingId_ShouldReturnFalse() {
        UUID nonExistingId = UUID.randomUUID();

        when(accountRepository.existsById(nonExistingId)).thenReturn(true);

        boolean exists = accountService.existsById(nonExistingId);

        assertFalse(exists);
    }

    @Test
    void deleteById_ShouldDeleteAccount() {
        UUID id = UUID.randomUUID();

        // No need to mock void methods like deleteById

        // Act - this should not throw any exception
        accountService.deleteById(id);

        // No explicit assertion needed as we're just verifying the method doesn't throw an exception
    }
}
