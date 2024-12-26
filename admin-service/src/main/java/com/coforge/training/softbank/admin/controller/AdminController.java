package com.coforge.training.softbank.admin.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coforge.training.softbank.admin.dto.AccountRequestDTO;
import com.coforge.training.softbank.admin.service.AdminService;

/**
 * User   : Singuluri.Kumar
 * Date   : 13-Dec-2024
 * Time   : 11:55:26 am
 * Project:admin-service
 **/
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/admin")
public class AdminController {
 
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
 
    private final AdminService adminService;
 
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
 
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }
 
    @GetMapping("/account-requests")
    public ResponseEntity<List<AccountRequestDTO>> getPendingAccountRequests(
            @RequestParam String username,
            @RequestParam String password) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            List<AccountRequestDTO> pendingRequests = adminService.getPendingAccountRequests();
            return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
 
    @PutMapping("/account-requests/approve/{accountNo}")
    public ResponseEntity<String> approveAccount(@PathVariable String accountNo,
                                                 @RequestParam String username,
                                                 @RequestParam String password) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            String response = adminService.approveAccount(accountNo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

 
    @PutMapping("/account-requests/reject/{accountNo}")
    public ResponseEntity<String> rejectAccount(@PathVariable String accountNo,
                                                @RequestParam String username,
                                                @RequestParam String password) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            String response = adminService.rejectAccount(accountNo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/accounts/deposit")
    public ResponseEntity<String> depositMoney(@RequestParam String accountNo,
                                               @RequestParam BigDecimal amount,
                                               @RequestParam String username,
                                               @RequestParam String password) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            String response = adminService.depositMoney(accountNo, amount);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/accounts/withdraw")
    public ResponseEntity<String> withdrawMoney(@RequestParam String accountNo,
                                                @RequestParam BigDecimal amount,
                                                @RequestParam String username,
                                                @RequestParam String password) {
        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            String response = adminService.withdrawMoney(accountNo, amount);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
}
