package com.nttdata.controller;

import com.nttdata.dto.AccountDTO;
import com.nttdata.dto.MovementDTO;
import com.nttdata.dto.MovementReportDTO;
import com.nttdata.dto.response.BaseResponse;
import com.nttdata.model.AccountEntity;
import com.nttdata.model.MovementEntity;
import com.nttdata.service.IMovementService;
import com.nttdata.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/movimientos")
@RequiredArgsConstructor
public class MovementController {

    private final IMovementService service;

    private final MapperUtil mapperUtil;

    @GetMapping
    public ResponseEntity<BaseResponse> findAll(){
        List<MovementDTO> list = mapperUtil.mapList(service.findAll(), MovementDTO.class);

        return ResponseEntity.ok(BaseResponse.builder().data(list).build());
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('admin_client_role') or hasRole('user_client_role')")
    public ResponseEntity<BaseResponse> getAccountById(@PathVariable("id") Long id) {
        MovementEntity entity = service.findById(id, "Movements");

        return ResponseEntity.ok(BaseResponse.builder()
                .data(mapperUtil.map(entity, MovementDTO.class))
                .build());
    }

    @GetMapping("/reportes")
//    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<BaseResponse> reportMovementByDateAndClientId(@RequestParam(value = "clientId") String clientId,
                                                                          @RequestParam(value = "startDate") String startDate,
                                                                          @RequestParam(value = "endDate") String endDate) throws Exception {
        List<MovementReportDTO> movementReportVo = service.reportMovementByDateAndClientId(clientId, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
        return ResponseEntity.ok(BaseResponse.builder().data(movementReportVo).build());
    }

    @PostMapping
//    @PreAuthorize("hasRole('admin_client_role') or hasRole('user_client_role')")
    public ResponseEntity<BaseResponse> addMovement(@Valid @RequestBody MovementDTO request) throws Exception {
        service.saveMovement(request);
        return ResponseEntity.ok(BaseResponse.builder().data(request).build());
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<BaseResponse> deleteAccount(@PathVariable("id") Long id) {
        // Eliminar registro
        //service.delete(id);
        // Eliminado logico
        service.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }

}
