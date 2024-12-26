package com.coforge.training.softbank.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
* User   : Singuluri.Kumar
* Date   : 14-Dec-2024
* Time   : 11:27:54 pm
* Project:authentication-service
**/

@FeignClient(name = "user-registration-service", url = "http://localhost:8086/registration")
public interface UserRegistrationServiceFeignClient {
 
    @GetMapping("/verify-account")
    boolean verifyAccount(@RequestParam String accountNo);
}
