package com.nttdata.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountDTO {

    @EqualsAndHashCode.Include
    private Long accountId;;

    @NotNull(message = "{account.number.empty}")
    private Integer accountNumber;

    @NotEmpty(message = "{account.type.empty}")
    @Size(min = 5, max = 100, message = "{account.type.size}")
    private String accountType;

    @NotNull(message = "{initial.balance.empty}")
    private BigDecimal initialBalance;

    private Boolean status;

    @NotNull(message = "{person.id.empty}")
    private Long personId;
}
