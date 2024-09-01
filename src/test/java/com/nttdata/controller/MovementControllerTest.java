package com.nttdata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.dto.MovementDTO;
import com.nttdata.dto.MovementReportDTO;
import com.nttdata.model.MovementEntity;
import com.nttdata.service.IMovementService;
import com.nttdata.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
* Se usa para pruebas de integración centradas en la capa de controlador.
* Solo carga los componentes relacionados con la capa web (controladores, convertidores, etc.)
* y simula los beans de servicio necesarios
* */
//@WebMvcTest(MovementController.class)
@SpringBootTest
@AutoConfigureMockMvc
class MovementControllerTest {

    @MockBean
    private IMovementService service;

    @MockBean
    private MapperUtil mapperUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"admin_client_role", "user_client_role"})
    void testFindAll() throws Exception {
        // Datos de prueba
        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setMovementId(1L);
        movementDTO.setMovementValue(BigDecimal.valueOf(100.00));

        List<MovementDTO> movements = List.of(movementDTO);

        when(service.findAll()).thenReturn(List.of(new MovementEntity()));
        when(mapperUtil.mapList(anyList(), eq(MovementDTO.class))).thenReturn(movements);

        // Realizar la solicitud GET
        mockMvc.perform(get("/api/v1/movimientos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].movementId").value(1L))
                .andExpect(jsonPath("$.data[0].movementValue").value(100.00));

        // Verificación de la invocación de los mocks
        verify(service).findAll();
        verify(mapperUtil).mapList(anyList(), eq(MovementDTO.class));
    }

    @Test
    @WithMockUser(roles = {"admin_client_role", "user_client_role"})
    void testGetMovementById() throws Exception {
        Long movementId = 1L;

        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setMovementId(movementId);
        movementDTO.setMovementValue(BigDecimal.valueOf(100.00));

        MovementEntity movementEntity = new MovementEntity();
        movementEntity.setMovementId(movementId);

        when(service.findById(eq(movementId), anyString())).thenReturn(movementEntity);
        when(mapperUtil.map(any(MovementEntity.class), eq(MovementDTO.class))).thenReturn(movementDTO);

        // Realizar la solicitud GET
        mockMvc.perform(get("/api/v1/movimientos/{id}", movementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.movementId").value(movementId))
                .andExpect(jsonPath("$.data.movementValue").value(100.00));

        // Verificación de la invocación de los mocks
        verify(service).findById(eq(movementId), anyString());
        verify(mapperUtil).map(any(MovementEntity.class), eq(MovementDTO.class));
    }

    @Test
    @WithMockUser(roles = {"admin_client_role"})
    void testReportMovementByDateAndClientId() throws Exception {
        String clientId = "123";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();

        MovementReportDTO reportDTO = new MovementReportDTO();
        reportDTO.setMovementDate(Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()));
        reportDTO.setName("John Doe");
        reportDTO.setAccountNumber(456);
        reportDTO.setAccountType("Savings");
        reportDTO.setInitialBalance(BigDecimal.valueOf(1000.00));
        reportDTO.setMovementStatus(true);
        reportDTO.setMovementValue(BigDecimal.valueOf(100.00));
        reportDTO.setBalance(BigDecimal.valueOf(1100.00));

        List<MovementReportDTO> reports = Collections.singletonList(reportDTO);

        when(service.reportMovementByDateAndClientId(clientId, startDate, endDate)).thenReturn(reports);

        mockMvc.perform(get("/api/v1/movimientos/reportes")
                        .param("clientId", clientId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].Movimiento").value(reportDTO.getMovementValue()));

        verify(service).reportMovementByDateAndClientId(eq(clientId), eq(startDate), eq(endDate));
    }

    @Test
    @WithMockUser(roles = {"admin_client_role", "user_client_role"})
    void testAddMovement() throws Exception {
        MovementDTO request = new MovementDTO();
        request.setAccountId(1L);
        request.setMovementValue(BigDecimal.valueOf(100.00));

        doNothing().when(service).saveMovement(any(MovementDTO.class));

        mockMvc.perform(post("/api/v1/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.movementValue").value(100.00));

        verify(service).saveMovement(any(MovementDTO.class));
    }

    @Test
    @WithMockUser(roles = {"admin_client_role"})
    void testDeleteMovement() throws Exception {
        Long movementId = 1L;

        doNothing().when(service).deleteLogic(eq(movementId));

        mockMvc.perform(delete("/api/v1/movimientos/{id}", movementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service).deleteLogic(eq(movementId));
    }
}