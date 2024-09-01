package com.nttdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.dto.AccountDTO;
import com.nttdata.model.AccountEntity;
import com.nttdata.service.IAccountService;
import com.nttdata.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
@SpringBootTest
Esta anotación se usa para cargar el contexto completo de la aplicación.
Es adecuada para pruebas de integración completas que involucran múltiples capas
(controladores, servicios, repositorios, etc.)
**/
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {

    @MockBean
    private IAccountService service;

    @MockBean
    private MapperUtil mapperUtil;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = {"admin_client_role"})
    void testFindAllAccounts() throws Exception {
        // Datos
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(1L);
        accountDTO.setAccountNumber(1234);
        accountDTO.setAccountType("AHORRO");

        List<AccountDTO> accounts = List.of(accountDTO);

        when(service.findAll()).thenReturn(List.of(new AccountEntity()));
        when(mapperUtil.mapList(anyList(), eq(AccountDTO.class))).thenReturn(accounts);

        // Realizar la solicitud GET
        mockMvc.perform(get("/api/v1/cuentas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].accountId").value(1L))
                .andExpect(jsonPath("$.data[0].accountNumber").value(1234))
                .andExpect(jsonPath("$.data[0].accountType").value("AHORRO"));

        verify(service).findAll();
        verify(mapperUtil).mapList(anyList(), eq(AccountDTO.class));
    }

    @Test
    @WithMockUser(roles = {"admin_client_role"})
    void testGetAccountById() throws Exception {
        Long accountId = 1L;

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(accountId);
        accountDTO.setAccountNumber(1234);
        accountDTO.setAccountType("AHORRO");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(accountId);

        when(service.findById(eq(accountId), anyString())).thenReturn(accountEntity);
        when(mapperUtil.map(any(AccountEntity.class), eq(AccountDTO.class))).thenReturn(accountDTO);

        // Realizar la solicitud GET
        mockMvc.perform(get("/api/v1/cuentas/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountId").value(accountId))
                .andExpect(jsonPath("$.data.accountNumber").value(1234))
                .andExpect(jsonPath("$.data.accountType").value("AHORRO"));

        // Verificar
        verify(service).findById(eq(accountId), anyString());
        verify(mapperUtil).map(any(AccountEntity.class), eq(AccountDTO.class));
    }

    @Test
    @WithMockUser(roles = {"admin_client_role"})
    void testAddAccount() throws Exception {
        AccountDTO request = new AccountDTO();
        request.setAccountNumber(1234);
        request.setAccountType("AHORRO");
        request.setInitialBalance(BigDecimal.valueOf(100));
        request.setStatus(Boolean.TRUE);
        request.setPersonId(1L);

        AccountEntity savedEntity = new AccountEntity();
        savedEntity.setAccountId(1L);

        AccountDTO savedDTO = new AccountDTO();
        savedDTO.setAccountId(1L);
        savedDTO.setAccountNumber(1234);
        savedDTO.setAccountType("AHORRO");

        when(service.save(any(AccountEntity.class))).thenReturn(savedEntity);
        when(mapperUtil.map(any(AccountDTO.class), eq(AccountEntity.class))).thenReturn(savedEntity);
        when(mapperUtil.map(any(AccountEntity.class), eq(AccountDTO.class))).thenReturn(savedDTO);

        // Realizar la solicitud POST
        mockMvc.perform(post("/api/v1/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountId").value(1L))
                .andExpect(jsonPath("$.data.accountNumber").value(1234))
                .andExpect(jsonPath("$.data.accountType").value("AHORRO"));

        // Verificar
        verify(service).save(any(AccountEntity.class));
        verify(mapperUtil).map(any(AccountDTO.class), eq(AccountEntity.class));
        verify(mapperUtil).map(any(AccountEntity.class), eq(AccountDTO.class));
    }

    @Test
    @WithMockUser(roles = {"admin_client_role"})
    void testUpdateAccount() throws Exception {
        Long accountId = 1L;
        AccountDTO request = new AccountDTO();
        request.setAccountNumber(1234);
        request.setAccountType("AHORRO");
        request.setInitialBalance(BigDecimal.valueOf(100));
        request.setStatus(Boolean.TRUE);
        request.setPersonId(1L);

        AccountEntity updatedEntity = new AccountEntity();
        updatedEntity.setAccountId(accountId);

        AccountDTO updatedDTO = new AccountDTO();
        updatedDTO.setAccountId(accountId);
        updatedDTO.setAccountNumber(1234);
        updatedDTO.setAccountType("AHORRO");

        // Configurar mocks
        when(service.update(eq(accountId), any(AccountEntity.class))).thenReturn(updatedEntity);
        when(mapperUtil.map(any(AccountDTO.class), eq(AccountEntity.class))).thenReturn(updatedEntity);
        when(mapperUtil.map(any(AccountEntity.class), eq(AccountDTO.class))).thenReturn(updatedDTO);

        // Realizar la solicitud PUT
        mockMvc.perform(put("/api/v1/cuentas/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountId").value(accountId))
                .andExpect(jsonPath("$.data.accountNumber").value(1234))
                .andExpect(jsonPath("$.data.accountType").value("AHORRO"));

        // Verificación de la invocación de los mocks
        verify(service).update(eq(accountId), any(AccountEntity.class));
        verify(mapperUtil).map(any(AccountDTO.class), eq(AccountEntity.class));
        verify(mapperUtil).map(any(AccountEntity.class), eq(AccountDTO.class));
    }

    @Test
    @WithMockUser(roles = {"admin_client_role"})
    void testDeleteAccount() throws Exception {
        Long accountId = 1L;

        doNothing().when(service).deleteLogic(eq(accountId));

        // Realizar la solicitud DELETE
        mockMvc.perform(delete("/api/v1/cuentas/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verificación de la invocación de los mocks
        verify(service).deleteLogic(eq(accountId));
    }

}
