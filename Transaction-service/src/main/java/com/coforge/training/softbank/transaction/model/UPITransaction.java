package com.coforge.training.softbank.transaction.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 4:11:32 pm
* Project:Transaction-service
**/

@Entity
@Table(name = "upi_transactions")
@Getter
@Setter
public class UPITransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_upi_id", nullable = false)
    private String fromUpiId;

    @Column(name = "to_upi_id", nullable = false)
    private String toUpiId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType; // UPI

    private String remarks;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "from_account_balance_after_transaction", nullable = false)
    private BigDecimal fromAccountBalanceAfterTransaction;

    @Column(name = "to_account_balance_after_transaction", nullable = false)
    private BigDecimal toAccountBalanceAfterTransaction;
}