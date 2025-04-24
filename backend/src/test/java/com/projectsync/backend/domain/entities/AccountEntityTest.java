package com.projectsync.backend.domain.entities;


import com.projectsync.backend.TestDataUtil;
import com.projectsync.backend.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AccountEntityTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void thatItShouldSaveAnAccount() {
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(0);
        accountRepository.save(accountEntity);
        assert accountRepository.findById(accountEntity.getId()).isPresent();
    }

    @Test
    void thatItShouldDeleteAnAccount() {
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(0);
        accountRepository.save(accountEntity);
        accountRepository.delete(accountEntity);
    }

    @Test
    void thatItShouldFindAnAccountByEmail() {
        AccountEntity accountEntity = TestDataUtil.createAccountEntities().get(0);
        accountRepository.save(accountEntity);

        Optional<AccountEntity> account = accountRepository.findByEmail(accountEntity.getEmail());
        assertTrue(account.isPresent());
        assertEquals( accountEntity.getId(), account.get().getId());

    }
}
