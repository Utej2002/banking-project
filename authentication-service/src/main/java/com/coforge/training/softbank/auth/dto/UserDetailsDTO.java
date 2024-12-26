package com.coforge.training.softbank.auth.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
 
public class UserDetailsDTO {
	private String username;
    private String email;
    private String accountNo;
    private BigDecimal balance;
    private String upiId;
    public UserDetailsDTO(String username, String email, String accountNo, BigDecimal balance, String upiId) {
        this.username = username;
        this.email = email;
        this.accountNo = accountNo;
        this.balance = balance;
        this.upiId = upiId;
    }
    
}
