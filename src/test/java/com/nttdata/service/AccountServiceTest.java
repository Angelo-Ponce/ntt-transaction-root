package com.nttdata.service;

import com.nttdata.exception.ModelNotFoundException;
import com.nttdata.model.AccountEntity;
import com.nttdata.repository.IAccountRepository;
import com.nttdata.service.Impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private IAccountRepository repository;

    @InjectMocks
    private AccountServiceImpl service;

    @Test
    void testDeleteLogic_WhenIdExists() {

        Long accountId = 1L;
        AccountEntity account = new AccountEntity();
        account.setAccountId(accountId);
        account.setStatus(true);

        when(repository.findById(accountId)).thenReturn(Optional.of(account));

        service.deleteLogic(accountId);

        assertFalse(account.getStatus());
        verify(repository).save(account);
    }

    @Test
    void testDeleteLogic_WhenIdDoesNotExist() {
        Long accountId = 1L;
        when(repository.findById(accountId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ModelNotFoundException.class, () -> service.deleteLogic(accountId));
        assertEquals("ID not found: " + accountId, exception.getMessage());
    }

    @Test
    void testSave() {
        AccountEntity account = new AccountEntity();
        when(repository.save(any(AccountEntity.class))).thenReturn(account);

        AccountEntity savedAccount = service.save(account);

        assertNotNull(savedAccount);
        verify(repository).save(account);
    }
}
