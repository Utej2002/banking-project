package com.coforge.training.softbank.admin.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coforge.training.softbank.admin.dto.AccountRequestDTO;
import com.coforge.training.softbank.admin.feign.AuthenticationFeignClient;
import com.coforge.training.softbank.admin.feign.UserRegistrationFeignClient;

/**
* User   : Singuluri.Kumar
* Date   : 13-Dec-2024
* Time   : 11:50:21 am
* Project:admin-service
**/

@Service
public class AdminService {
	

    private final UserRegistrationFeignClient userRegistrationFeignClient;
	
    private final AuthenticationFeignClient authenticationFeignClient;

    public AdminService(UserRegistrationFeignClient userRegistrationFeignClient,AuthenticationFeignClient authenticationFeignClient) {
        this.userRegistrationFeignClient = userRegistrationFeignClient;
        this.authenticationFeignClient=authenticationFeignClient;
    }

    public List<AccountRequestDTO> getPendingAccountRequests() {
        return userRegistrationFeignClient.getPendingAccountRequests();
    }

    public String approveAccount(String accountNo) {
        return userRegistrationFeignClient.approveAccount(accountNo);
    }

    public String rejectAccount(String accountNo) {
        return userRegistrationFeignClient.rejectAccount(accountNo);
    }

    public String depositMoney(String accountNo, BigDecimal amount) {
        return authenticationFeignClient.depositMoney(accountNo, amount);
    }

    public String withdrawMoney(String accountNo, BigDecimal amount) {
        return authenticationFeignClient.withdrawMoney(accountNo, amount);
    }
}
