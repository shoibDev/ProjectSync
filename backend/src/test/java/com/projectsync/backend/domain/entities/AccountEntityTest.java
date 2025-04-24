package com.projectsync.backend.domain.entities;


import com.projectsync.backend.TestDataUtil;
import com.projectsync.backend.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
}
