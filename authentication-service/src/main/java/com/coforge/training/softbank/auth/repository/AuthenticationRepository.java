package com.coforge.training.softbank.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coforge.training.softbank.auth.model.AuthenticationModel;

/**
 * User   : Singuluri.Kumar
 * Date   : 13-Dec-2024
 * Time   : 8:27:35 pm
 * Project:authentication-service
 **/

public interface AuthenticationRepository  extends JpaRepository<AuthenticationModel, Long> {
	Optional<AuthenticationModel> findByUsername(String username);
	Optional<AuthenticationModel> findByEmail(String email);
	Optional<AuthenticationModel> findByAccountNo(String accountNo);
	Optional<AuthenticationModel> findByAuthPin(String authPin);
	Optional<AuthenticationModel> findByUpiId(String upiId);


}
