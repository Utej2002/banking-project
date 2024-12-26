package com.coforge.training.softbank.admin.feign;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="authentication-service", url="http://localhost:8084/auth")
public interface AuthenticationFeignClient {

    @PostMapping("/deposit")
    String depositMoney(@RequestParam("accountNo") String accountNo, @RequestParam("amount") BigDecimal amount);

    @PostMapping("/withdraw")
    String withdrawMoney(@RequestParam("accountNo") String accountNo, @RequestParam("amount") BigDecimal amount);
}