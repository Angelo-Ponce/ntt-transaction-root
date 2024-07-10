package com.nttdata.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="account")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long accountId;

    @Column(name = "account_number", nullable = false, unique = true)
    private Integer accountNumber;

    @Column(name = "account_type", nullable = false, length = 100)
    private String accountType;

    @Column(name = "initial_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal initialBalance;

    @Column(nullable = false, length = 1)
    private Boolean status;

//    @Column(name = "person_id", nullable = false)
//    private Long personId;
//
//    @ManyToOne(fetch = FetchType.LAZY) // FK
//    @JoinColumn(name = "person_id", referencedColumnName = "person_id", foreignKey = @ForeignKey(name = "FK_ACCOUNT_CLIENT"))
//    private ClientView client;
    @Column(name = "person_id", nullable = false)
    private Long personId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", foreignKey = @ForeignKey(name = "FK_acc_client"), insertable = false, updatable = false)
    private ClientView client;
}