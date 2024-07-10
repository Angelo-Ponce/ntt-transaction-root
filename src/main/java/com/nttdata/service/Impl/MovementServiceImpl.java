package com.nttdata.service.Impl;

import com.nttdata.dto.MovementDTO;
import com.nttdata.dto.MovementReportDTO;
import com.nttdata.exception.ModelNotFoundException;
import com.nttdata.model.AccountEntity;
import com.nttdata.model.MovementEntity;
import com.nttdata.repository.IGenericRepository;
import com.nttdata.repository.IMovementRepository;
import com.nttdata.service.IAccountService;
import com.nttdata.service.IMovementService;
import com.nttdata.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovementServiceImpl extends CRUDImpl<MovementEntity, Long> implements IMovementService {

    private final IMovementRepository repository;

    private final IAccountService accountService;

    private final MapperUtil mapperUtil;


    @Override
    protected IGenericRepository<MovementEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public void deleteLogic(Long id) {
        MovementEntity entity = repository.findById(id).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
        entity.setStatus(Boolean.FALSE);
        repository.save(entity);
    }

    @Transactional
    @Override
    public void saveMovement(MovementDTO request) {
        // Obtener cuenta
        AccountEntity accountEntity = this.accountService.findById(request.getAccountId(), "Account");

        if (request.getMovementValue().compareTo(BigDecimal.ZERO) > 0){
            request.setMovementType("DEPOSITO");
        } else if(request.getMovementValue().compareTo(BigDecimal.ZERO) < 0){
            if( accountEntity.getInitialBalance().compareTo(BigDecimal.ZERO) == 0 ||
                    accountEntity.getInitialBalance().compareTo(request.getMovementValue()) < 0){
                throw new ModelNotFoundException("Saldo no disponible");
            }
            request.setMovementType("RETIRO");
        } else {
            throw new ModelNotFoundException("Movimiento no valido");
        }

        // Actualizar el saldo inicial
        accountEntity.setInitialBalance(accountEntity.getInitialBalance().add(request.getMovementValue()));
        request.setMovementDate(new Date());
        request.setBalance(accountEntity.getInitialBalance());
        request.setStatus(Boolean.TRUE);
        this.save(request);
        this.accountService.save(accountEntity);
    }

    @Override
    public List<MovementReportDTO> reportMovementByDateAndClientId(String clientId, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        List<MovementEntity> movementVo = this.repository.reportMovement(clientId, getDateTransform(startDate), getDateTransform(endDate));
        List<MovementReportDTO> reportVos = new ArrayList<>();
        if (!movementVo.isEmpty()) {
            movementVo.forEach( data -> {
                MovementReportDTO movement = MovementReportDTO.builder()
                        .movementDate(data.getMovementDate())
                        .name(data.getAccount().getClient().getName())
                        .accountNumber(data.getAccount().getAccountNumber())
                        .accountType(data.getAccount().getAccountType())
                        .initialBalance(data.getAccount().getInitialBalance())
                        .movementStatus(data.getStatus())
                        .movementValue(data.getMovementValue())
                        .balance(data.getBalance())
                        .build();
                reportVos.add(movement);
            });
        }
        return reportVos;
    }


    private void save(MovementDTO movementDTO) {
        MovementEntity movementEntity = mapperUtil.map(movementDTO, MovementEntity.class);
        repository.save(movementEntity);
        movementDTO.setMovementId(movementEntity.getMovementId());
    }

    private Date getDateTransform ( LocalDateTime localDateTime ){
        // Convertir a ZonedDateTime
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

        // Convertir a Instant
        Instant instant = zonedDateTime.toInstant();

        // Crear Date
        return Date.from(instant);
    }
}
