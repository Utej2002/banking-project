package com.coforge.training.softbank.transaction.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coforge.training.softbank.transaction.dto.NEFTTransactionDTO;
import com.coforge.training.softbank.transaction.dto.UPITransactionDTO;
import com.coforge.training.softbank.transaction.model.NEFTTransaction;
import com.coforge.training.softbank.transaction.model.UPITransaction;
import com.coforge.training.softbank.transaction.service.TransactionService;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 11:55:28 am
* Project:Transaction-service
**/
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/neft")
    public ResponseEntity<String> performNeftTransaction(@RequestBody NEFTTransactionDTO transactionDTO) {
        transactionDTO.setTransactionType("NEFT");
        String response = transactionService.performNeftTransaction(transactionDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/upi")
    public ResponseEntity<String> performUpiTransaction(@RequestBody UPITransactionDTO transactionDTO) {
        transactionDTO.setTransactionType("UPI");
        String response = transactionService.performUpiTransaction(transactionDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/history/neft/{accountNo}")
    public ResponseEntity<List<NEFTTransaction>> getNeftTransactionHistory(@PathVariable String accountNo) {
        List<NEFTTransaction> transactions = transactionService.getNeftTransactionHistory(accountNo);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/history/upi/{upiId}")
    public ResponseEntity<List<UPITransaction>> getUpiTransactionHistory(@PathVariable String upiId) {
        List<UPITransaction> transactions = transactionService.getUpiTransactionHistory(upiId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/history/neft")
    public ResponseEntity<List<NEFTTransactionDTO>> getNeftTransactionHistory(@RequestParam String accountNo, @RequestParam String startDate, @RequestParam String endDate) {
        List<NEFTTransaction> transactions = transactionService.getNeftTransactionHistory(accountNo, startDate, endDate);
        List<NEFTTransactionDTO> transactionDTOs = transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(transactionDTOs, HttpStatus.OK);
    }
 
    @GetMapping("/history/upi")
    public ResponseEntity<List<UPITransactionDTO>> getUpiTransactionHistory(@RequestParam String upiId, @RequestParam String startDate, @RequestParam String endDate) {
        List<UPITransaction> transactions = transactionService.getUpiTransactionHistory(upiId, startDate, endDate);
        List<UPITransactionDTO> transactionDTOs = transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(transactionDTOs, HttpStatus.OK);
    }
 
    private NEFTTransactionDTO convertToDTO(NEFTTransaction transaction) {
        NEFTTransactionDTO dto = new NEFTTransactionDTO();
        dto.setId(transaction.getId());
        dto.setFromAccount(transaction.getFromAccount());
        dto.setToAccount(transaction.getToAccount());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setRemarks(transaction.getRemarks());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setFromAccountBalanceAfterTransaction(transaction.getFromAccountBalanceAfterTransaction());
        dto.setToAccountBalanceAfterTransaction(transaction.getToAccountBalanceAfterTransaction());
        return dto;
    }
 
    private UPITransactionDTO convertToDTO(UPITransaction transaction) {
        UPITransactionDTO dto = new UPITransactionDTO();
        dto.setId(transaction.getId());
        dto.setFromUpiId(transaction.getFromUpiId());
        dto.setToUpiId(transaction.getToUpiId());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setRemarks(transaction.getRemarks());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setFromAccountBalanceAfterTransaction(transaction.getFromAccountBalanceAfterTransaction());
        dto.setToAccountBalanceAfterTransaction(transaction.getToAccountBalanceAfterTransaction());

        return dto;
    }
}