package com.nttdata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MovementReportDTO {

    @JsonProperty("Fecha")
    private Date movementDate;

    @JsonProperty("Cliente")
    private String name;

    @JsonProperty("NumeroCuenta")
    private Integer accountNumber;

    @JsonProperty("Tipo")
    private String accountType;

    @JsonProperty("SaldoInicial")
    private BigDecimal initialBalance;

    @JsonProperty("Estado")
    private Boolean movementStatus;

    @JsonProperty("Movimiento")
    private BigDecimal movementValue;

    @JsonProperty("SaldoDisponible")
    private BigDecimal balance;
}
