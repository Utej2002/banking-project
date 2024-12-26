package com.coforge.training.softbank.reg.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
* User   : Singuluri.Kumar
* Date   : 14-Dec-2024
* Time   : 8:51:42 pm
* Project:registration-service
**/

@FeignClient(name = "auth-service", url = "http://localhost:8084/auth")
public interface AuthServiceFeignClient {
 
    @PostMapping("/register")
    String registerUser(@RequestBody AuthUserRequest authUserRequest);
 
    class AuthUserRequest {
        private String username;
        private String email;
        private String accountNo;
        private String authPin;
        private String loginPassword;
        private String transactionPassword;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getAccountNo() {
			return accountNo;
		}
		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}
		public String getAuthPin() {
			return authPin;
		}
		public void setAuthPin(String authPin) {
			this.authPin = authPin;
		}
		public String getLoginPassword() {
			return loginPassword;
		}
		public void setLoginPassword(String loginPassword) {
			this.loginPassword = loginPassword;
		}
		public String getTransactionPassword() {
			return transactionPassword;
		}
		public void setTransactionPassword(String transactionPassword) {
			this.transactionPassword = transactionPassword;
		}
 
        // Getters and Setters
        
    }
}
