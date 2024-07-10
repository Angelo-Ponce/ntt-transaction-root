package com.nttdata.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="movement")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MovementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long movementId;

    @ManyToOne(fetch = FetchType.LAZY) // FK
    @JoinColumn(name = "accoun_id", nullable = false, foreignKey = @ForeignKey(name = "FK_MOVEMENT_ACCOUNT"))
    private AccountEntity account;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "movement_date", nullable = false)
    private Date movementDate;

    @Column(name = "movement_type", nullable = false, length = 150)
    private String movementType;

    @Column(name = "movement_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal movementValue;

    @Column(name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, length = 1)
    private Boolean status;
}