package com.coforge.training.softbank.dashboard.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coforge.training.softbank.dashboard.dto.UserDetailsDTO;

/**
* User   : Singuluri.Kumar
* Date   : 17-Dec-2024
* Time   : 11:03:14 am
* Project:dashboard-service
**/

@FeignClient(name = "authentication-service", url = "http://localhost:8084/auth")
public interface AuthenticationFeignClient {

    @GetMapping("/details")
    UserDetailsDTO getUserDetails(@RequestParam String accountNo);
    
    @GetMapping("/user-details")
    UserDetailsDTO getUserDetailsByUsername(@RequestParam("username") String username);
}
