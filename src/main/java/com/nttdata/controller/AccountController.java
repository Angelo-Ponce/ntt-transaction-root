package com.nttdata.controller;

import com.nttdata.dto.AccountDTO;
import com.nttdata.dto.response.BaseResponse;
import com.nttdata.model.AccountEntity;
import com.nttdata.service.IAccountService;
import com.nttdata.util.MapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService service;

    private final MapperUtil mapperUtil;

    @GetMapping
    @PreAuthorize("hasRole('admin_client_role') or hasRole('user_client_role')")
    public ResponseEntity<BaseResponse> findAll(){
        List<AccountDTO> list = mapperUtil.mapList(service.findAll(), AccountDTO.class);

        return ResponseEntity.ok(BaseResponse.builder().data(list).build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin_client_role') or hasRole('user_client_role')")
    public ResponseEntity<BaseResponse> getAccountById(@PathVariable("id") Long id) {
        AccountEntity entity = service.findById(id, "Account");

        return ResponseEntity.ok(BaseResponse.builder()
                .data(mapperUtil.map(entity, AccountDTO.class))
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('admin_client_role') or hasRole('user_client_role')")
    public ResponseEntity<BaseResponse> addAccount(@Valid @RequestBody AccountDTO request) {
        AccountEntity account = service.save(mapperUtil.map(request, AccountEntity.class));

        return ResponseEntity.ok(BaseResponse.builder().data(mapperUtil.map(account, AccountDTO.class)).build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin_client_role') or hasRole('user_client_role')")
    public ResponseEntity<BaseResponse> updateAccount(@PathVariable("id") Long id,
                                                      @RequestBody AccountDTO dto) {
        dto.setAccountId(id);
        AccountEntity entity = service.update(id, mapperUtil.map(dto, AccountEntity.class));
        return ResponseEntity.ok(BaseResponse.builder().data(mapperUtil.map(entity, AccountDTO.class)).build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<BaseResponse> deleteAccount(@PathVariable("id") Long id) {
        // Eliminar registro
        //service.delete(id);
        // Eliminado logico
        service.deleteLogic(id);
        return ResponseEntity.noContent().build();
    }
}
