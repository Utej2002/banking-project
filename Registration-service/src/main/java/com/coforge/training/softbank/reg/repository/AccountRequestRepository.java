package com.coforge.training.softbank.reg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coforge.training.softbank.reg.model.AccountRequest;

/**
* User   : Singuluri.Kumar
* Date   : 14-Dec-2024
* Time   : 8:47:46 pm
* Project:registration-service
**/

public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {
	
	 Optional<AccountRequest> findByAccountNo(String accountNo);
	 List<AccountRequest> findByStatus(String status);
}
