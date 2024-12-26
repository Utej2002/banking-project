package com.coforge.training.softbank.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 9:37:39 pm
* Project:dashboard-service
**/

@Getter
@Setter
public class TransactionSummaryDTO {
    private Long id;
    private String fromAccount;
    private String toAccount;
    private String fromUpiId;
    private String toUpiId;
    private BigDecimal amount;
    private String transactionType;
    private String remarks;
    private LocalDateTime transactionDate;
    private BigDecimal fromAccountBalanceAfterTransaction;
    private BigDecimal toAccountBalanceAfterTransaction;
    }
