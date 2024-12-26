package com.coforge.training.softbank.transaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coforge.training.softbank.transaction.model.Payee;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 11:49:18 am
* Project:Transaction-service
**/

public interface PayeeRepository extends JpaRepository<Payee, Long> {
    boolean existsByAccountNo(String accountNo);
    boolean existsByUpiId(String upiId);
    Optional<Payee> findByAccountNo(String accountNo);
    Optional<Payee> findByUpiId(String upiId);
}
