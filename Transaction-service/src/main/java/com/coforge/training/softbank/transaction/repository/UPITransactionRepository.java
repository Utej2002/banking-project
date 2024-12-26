package com.coforge.training.softbank.transaction.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coforge.training.softbank.transaction.model.UPITransaction;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 4:14:56 pm
* Project:Transaction-service
**/

public interface UPITransactionRepository extends JpaRepository<UPITransaction, Long> {
    List<UPITransaction> findByFromUpiId(String fromUpiId);
    List<UPITransaction> findByFromUpiIdAndToUpiId(String fromUpiId, String toUpiId);
    List<UPITransaction> findByFromUpiIdAndTransactionDateBetween(String fromUpiId, LocalDateTime startDate, LocalDateTime endDate);
    List<UPITransaction> findByFromUpiIdOrToUpiIdAndTransactionDateBetween(String fromUpiId, String toUpiId, LocalDateTime startDate, LocalDateTime endDate);
    
}