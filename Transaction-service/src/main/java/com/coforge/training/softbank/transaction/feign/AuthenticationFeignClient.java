package com.coforge.training.softbank.transaction.feign;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 11:52:27 am
* Project:Transaction-service
**/

@FeignClient(name = "authentication-service", url = "http://localhost:8084/auth")
public interface AuthenticationFeignClient {
    @GetMapping("/balance")
    BigDecimal getBalance(@RequestParam String accountNo);

    @GetMapping("/update-balance")
    void updateBalance(@RequestParam String accountNo, @RequestParam BigDecimal newBalance);

    @GetMapping("/verify-account")
    boolean verifyAccount(@RequestParam String accountNo);

    @GetMapping("/verify-upi")
    boolean verifyUpi(@RequestParam String upiId);

    @GetMapping("/balance-by-upi")
    BigDecimal getBalanceByUpi(@RequestParam String upiId);

    @GetMapping("/update-balance-by-upi")
    void updateBalanceByUpi(@RequestParam String upiId, @RequestParam BigDecimal newBalance);
    @PostMapping("/verify-transaction-password")
    boolean verifyTransactionPassword(@RequestParam String accountNo, @RequestParam String transactionPassword);

    @PostMapping("/verify-transaction-password-upi")
    boolean verifyTransactionPasswordByUpi(@RequestParam String upiId, @RequestParam String transactionPassword);
}
