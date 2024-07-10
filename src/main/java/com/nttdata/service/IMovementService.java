package com.nttdata.service;

import com.nttdata.dto.MovementDTO;
import com.nttdata.dto.MovementReportDTO;
import com.nttdata.model.MovementEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface IMovementService extends ICRUD<MovementEntity, Long> {

    void deleteLogic(Long id);

    void saveMovement(MovementDTO request);

    /**
     * Report movement by date and client id
     * @param clientId client id
     * @param startDate start date
     * @param endDate end date
     * @return
     * @throws Exception
     */
    List<MovementReportDTO> reportMovementByDateAndClientId(String clientId, LocalDateTime startDate, LocalDateTime endDate) throws Exception;
}
