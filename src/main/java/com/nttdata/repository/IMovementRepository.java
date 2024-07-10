package com.nttdata.repository;

import com.nttdata.model.MovementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IMovementRepository extends IGenericRepository<MovementEntity, Long> {

    @Query("select m FROM movement m where m.account.client.clientId = :clientId and m.movementDate between :startDate and :endDate")
    List<MovementEntity> reportMovement(@Param("clientId") String clientId,
                                        @Param("startDate") Date startDate,
                                        @Param("endDate") Date endDate);
}
