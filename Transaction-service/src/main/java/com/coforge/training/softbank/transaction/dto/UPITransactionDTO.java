package com.coforge.training.softbank.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 3:58:30 pm
* Project:Transaction-service
**/

@Getter
@Setter
public class UPITransactionDTO {
    private Long id;
    private String fromUpiId;
    private String toUpiId;
    private BigDecimal amount;
    private String transactionType;
    private String remarks;
    private LocalDateTime transactionDate;
    private BigDecimal fromAccountBalanceAfterTransaction;
    private BigDecimal toAccountBalanceAfterTransaction;
    private String transactionPassword;
}