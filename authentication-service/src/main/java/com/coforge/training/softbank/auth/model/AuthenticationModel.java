package com.coforge.training.softbank.auth.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User   : Singuluri.Kumar
 * Date   : 13-Dec-2024
 * Time   : 8:01:40 pm
 * Project:authentication-service
 **/
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthenticationModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "dealer_seq")
	@SequenceGenerator(name = "dealer_seq",initialValue = 60001,allocationSize = 1)	
	@Column(name = "user_id")
	private Long userId;

	@Column(nullable = false, unique = true)
	 private String username;
	
	 @Column(nullable = false)
	 private String loginPassword;
	 
	 @Column(nullable = false)
	 private String transactionPassword;
	 
	 @Column(nullable = false, unique = true)
	 private String email;
	 
	 @Column(nullable = false, unique = true)
	 private String accountNo;
	 @Column(nullable = false, unique = true)
	 private String authPin;
	 @Column(name = "role", nullable = false)
	 private String role = "ROLE_USER"; // Fixed role
	 @Column(name = "is_locked", nullable = false)
	 private boolean isLocked = false;
	 @Column(name = "failed_attempts", nullable = false)
	 private int failedAttempts = 0;
	 @Column(name = "created_at", nullable = false)
	 private LocalDateTime createdAt;
	 
	 @Column(name = "balance", nullable = false)
	 private BigDecimal balance; // Add balance field
	 
	 @Column(name = "updated_at")
	 private LocalDateTime updatedAt;
	 
	 @Column(nullable = false, unique = true)
	 private String upiId;
	}