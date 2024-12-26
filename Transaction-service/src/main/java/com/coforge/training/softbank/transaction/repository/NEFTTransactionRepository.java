package com.coforge.training.softbank.transaction.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coforge.training.softbank.transaction.model.NEFTTransaction;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 11:48:25 am
* Project:Transaction-service
**/

public interface NEFTTransactionRepository extends JpaRepository<NEFTTransaction, Long> {
    List<NEFTTransaction> findByFromAccount(String fromAccount);
    List<NEFTTransaction> findByFromAccountAndToAccount(String fromAccount, String toAccount);
    List<NEFTTransaction> findByFromAccountAndTransactionDateBetween(String fromAccount, LocalDateTime startDate, LocalDateTime endDate);
    List<NEFTTransaction> findByFromAccountOrToAccountAndTransactionDateBetween(String fromAccount, String toAccount, LocalDateTime startDate, LocalDateTime endDate);
}