package com.nttdata.service;

import com.nttdata.dto.MovementDTO;
import com.nttdata.dto.MovementReportDTO;
import com.nttdata.exception.ModelNotFoundException;
import com.nttdata.model.AccountEntity;
import com.nttdata.model.ClientView;
import com.nttdata.model.MovementEntity;
import com.nttdata.repository.IMovementRepository;
import com.nttdata.service.Impl.MovementServiceImpl;
import com.nttdata.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MovementServiceTest {

    @MockBean
    private IMovementRepository repository;

    @MockBean
    private IAccountService accountService;

    @MockBean
    private MapperUtil mapperUtil;

    @Autowired
    private MovementServiceImpl service;

    @Test
    void testDeleteLogic_WhenIdExists() {
        Long movementId = 1L;
        MovementEntity movement = new MovementEntity();
        movement.setMovementId(movementId);
        movement.setStatus(true);

        when(repository.findById(movementId)).thenReturn(Optional.of(movement));

        service.deleteLogic(movementId);

        assertFalse(movement.getStatus());
        verify(repository).save(movement);
    }

    @Test
    void testDeleteLogic_WhenIdDoesNotExist() {
        Long movementId = 1L;
        when(repository.findById(movementId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ModelNotFoundException.class, () -> service.deleteLogic(movementId));
        assertEquals("ID not found: " + movementId, exception.getMessage());
    }

    @Transactional
    @Test
    void testSaveMovement_Deposit() {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setAccountId(1L);
        movementDTO.setMovementValue(BigDecimal.valueOf(100));

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setInitialBalance(BigDecimal.ZERO);

        when(accountService.findById(1L, "Account")).thenReturn(accountEntity);
        when(mapperUtil.map(any(MovementDTO.class), eq(MovementEntity.class))).thenReturn(new MovementEntity());

        service.saveMovement(movementDTO);

        assertEquals("DEPOSITO", movementDTO.getMovementType());
        verify(repository).save(any(MovementEntity.class));
        verify(accountService).save(any(AccountEntity.class));
    }

    @Transactional
    @Test
    void testSaveMovement_InsufficientBalance() {
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setAccountId(1L);
        movementDTO.setMovementValue(BigDecimal.valueOf(-100));

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setInitialBalance(BigDecimal.valueOf(50));

        when(accountService.findById(1L, "Account")).thenReturn(accountEntity);

        // Act & Assert
        Exception exception = assertThrows(ModelNotFoundException.class, () -> service.saveMovement(movementDTO));
        assertEquals("Saldo no disponible", exception.getMessage());
    }

    @Test
    void testReportMovementByDateAndClientId() throws Exception {
        String  clientId = "123";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        // Crear objetos simulados
        ClientView clientView = new ClientView();
        clientView.setName("Test Client");

        AccountEntity account = new AccountEntity();
        account.setAccountId(1L);
        account.setClient(clientView);
        account.setAccountNumber(1001);
        account.setAccountType("AHORRO");
        account.setInitialBalance(BigDecimal.valueOf(1000));
        account.setStatus(true);

        MovementEntity movement = new MovementEntity();
        movement.setAccount(account);
        movement.setMovementDate(new Date());
        movement.setMovementValue(BigDecimal.valueOf(100));
        List<MovementEntity> movementList = Collections.singletonList(movement);

        when(repository.reportMovement(eq(clientId), any(Date.class), any(Date.class))).thenReturn(movementList);

        List<MovementReportDTO> report = service.reportMovementByDateAndClientId(clientId, startDate, endDate);

        assertNotNull(report);
        assertFalse(report.isEmpty());
        verify(repository).reportMovement(eq(clientId), any(Date.class), any(Date.class));
    }
}
