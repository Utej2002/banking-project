package com.coforge.training.softbank.admin.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


import com.coforge.training.softbank.admin.dto.AccountRequestDTO;

/**
* User   : Singuluri.Kumar
* Date   : 15-Dec-2024
* Time   : 11:02:03 am
* Project:admin-service
**/

@FeignClient(name = "user-registration-service", url = "http://localhost:8086/registration")
public interface UserRegistrationFeignClient {

    @GetMapping("/account-requests")
    List<AccountRequestDTO> getPendingAccountRequests();

    @PutMapping("/account-requests/approve/{accountNo}")
    String approveAccount(@PathVariable String accountNo);

    @PutMapping("/account-requests/reject/{accountNo}")
    String rejectAccount(@PathVariable String accountNo);

//    @PostMapping("/accounts/deposit")
//    String depositMoney(@RequestParam("accountNo") String accountNo, @RequestParam("amount") Double amount);
//
//    @PostMapping("/accounts/withdraw")
//    String withdrawMoney(@RequestParam("accountNo") String accountNo, @RequestParam("amount") Double amount);
}
