package com.coforge.training.softbank.auth.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
* User   : Singuluri.Kumar
* Date   : 15-Dec-2024
* Time   : 11:56:51 pm
* Project:authentication-service
**/

@Getter
@Setter
public class AuthenticationDTO {
	 
    private String username;  // User's username
    private String accountNo; // User's account number
    private String email;     // User's email address
    private BigDecimal balance; // User's account balance
 
    // Getters and Setters
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getAccountNo() {
        return accountNo;
    }
 
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public BigDecimal getBalance() {
        return balance;
    }
 
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
