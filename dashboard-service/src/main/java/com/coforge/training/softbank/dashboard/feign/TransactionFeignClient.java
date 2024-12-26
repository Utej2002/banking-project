package com.coforge.training.softbank.dashboard.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coforge.training.softbank.dashboard.dto.TransactionSummaryDTO;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 9:43:19 pm
* Project:dashboard-service
**/

@FeignClient(name = "transaction-service", url = "http://localhost:8087/transactions")
public interface TransactionFeignClient {
    @GetMapping("/history/neft")
    List<TransactionSummaryDTO> getNeftTransactionHistory(@RequestParam String accountNo, @RequestParam String startDate, @RequestParam String endDate);

    @GetMapping("/history/upi")
    List<TransactionSummaryDTO> getUpiTransactionHistory(@RequestParam String upiId, @RequestParam String startDate, @RequestParam String endDate);
}
