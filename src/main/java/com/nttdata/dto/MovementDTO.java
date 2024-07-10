package com.nttdata.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MovementDTO {

    @EqualsAndHashCode.Include
    private Long movementId;

    @NotNull(message = "{account.id.empty}")
    private Long accountId;

    private Date movementDate;

    private String movementType;

    @NotNull(message = "{movement.value}")
    private BigDecimal movementValue;

    private BigDecimal balance;

    private Boolean status;
}
