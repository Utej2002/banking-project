package com.coforge.training.softbank.auth.controller;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coforge.training.softbank.auth.dto.UserDetailsDTO;
import com.coforge.training.softbank.auth.feign.UserRegistrationServiceFeignClient;
import com.coforge.training.softbank.auth.model.AuthenticationModel;
import com.coforge.training.softbank.auth.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
    private final AuthenticationService userService;
 
    @Autowired
    private final UserRegistrationServiceFeignClient userRegistrationServiceFeignClient;
 
    public AuthenticationController(AuthenticationService userService,
                                    UserRegistrationServiceFeignClient userRegistrationServiceFeignClient) {
        this.userService = userService;
        this.userRegistrationServiceFeignClient = userRegistrationServiceFeignClient;
    }
 
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        String accountNo = request.get("accountNo");
        String authPin = request.get("authPin");
        String loginPassword = request.get("loginPassword");
        String transactionPassword = request.get("transactionPassword");
 
        // Validate if account exists and is approved by admin
        boolean isAccountValid = userRegistrationServiceFeignClient.verifyAccount(accountNo);
 
        if (!isAccountValid) {
            return new ResponseEntity<>("Account number is not approved or does not exist.", HttpStatus.BAD_REQUEST);
        }
 
        try {
            // Register the user in Authentication Service
            userService.registerUser(username, email, accountNo, authPin, loginPassword, transactionPassword);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

	
	//new
	@PostMapping("/forgot-username")
    public ResponseEntity<String> forgotUsername(@RequestBody Map<String, String> request) {
        String authPin = request.get("authPin");
        if (authPin == null || authPin.isEmpty()) {
            return new ResponseEntity<>("Auth Pin is required.", HttpStatus.BAD_REQUEST);
        }
        try {
            String username = userService.getUsernameByAuthPin(authPin);
            return new ResponseEntity<>("Your username is: " + username, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
	
	@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        System.out.println("Request received: " + request);
        String username = request.get("username");
        String password = request.get("password");
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return new ResponseEntity<>("Required parameters 'username' and 'password' are not present.", HttpStatus.BAD_REQUEST);
        }
        return userService.findByUsername(username)
                .map(user -> {
                    if (user.isLocked()) {
                        return new ResponseEntity<>("Account is locked", HttpStatus.FORBIDDEN);
                    }
                    if (userService.verifyLoginPassword(user, password)) {
                        userService.resetFailedAttempts(user);
                        return new ResponseEntity<>("Login successful", HttpStatus.OK);
                    } else {
                        userService.incrementFailedAttempts(user);
                        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
                    }
                })
                .orElse(new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND));
    }

	@PostMapping("/reset-login-password")
	public ResponseEntity<String> resetLoginPassword(@RequestBody Map<String, String> request) {
	    String authPin = request.get("authPin");
	    String newPassword = request.get("newPassword");
	    if (authPin == null || authPin.isEmpty() || newPassword == null || newPassword.isEmpty()) {
	        return new ResponseEntity<>("Required parameters 'authPin' and 'newPassword' are not present.", HttpStatus.BAD_REQUEST);
	    }
	    try {
	        userService.resetLoginPassword(authPin, newPassword);
	        return new ResponseEntity<>("Login password reset successfully.", HttpStatus.OK);
	    } catch (IllegalArgumentException ex) {
	        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}
	
    @PostMapping("/reset-transaction-password")
    public ResponseEntity<String> resetTransactionPassword(@RequestBody Map<String, String> request) {
        String authPin = request.get("authPin");
        String newPassword = request.get("newPassword");
        if (authPin == null || authPin.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return new ResponseEntity<>("Required parameters 'authPin' and 'newPassword' are not present.", HttpStatus.BAD_REQUEST);
        }
        try {
            userService.resetTransactionPassword(authPin, newPassword);
            return new ResponseEntity<>("Transaction password reset successfully.", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
  
    
    // Endpoint to fetch user balance
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam String accountNo) {
        BigDecimal balance = userService.getBalance(accountNo);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    // Endpoint to update user balance
    @GetMapping("/update-balance")
    public ResponseEntity<Void> updateBalance(@RequestParam String accountNo, @RequestParam BigDecimal newBalance) {
        userService.updateBalance(accountNo, newBalance);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Endpoint to verify if an account exists
    @GetMapping("/verify-account")
    public ResponseEntity<Boolean> verifyAccount(@RequestParam("accountNo") String accountNo) {
        boolean exists = userService.verifyAccount(accountNo);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    // Endpoint to verify if a UPI ID exists
    @GetMapping("/verify-upi")
    public ResponseEntity<Boolean> verifyUpi(@RequestParam String upiId) {
        boolean exists = userService.verifyUpi(upiId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    // Endpoint to fetch user balance by username
    @GetMapping("/balance/{username}")
    public ResponseEntity<BigDecimal> getBalanceByUsername(@PathVariable String username) {
        Optional<AuthenticationModel> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            AuthenticationModel user = userOpt.get();
            return ResponseEntity.ok(user.getBalance());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/balance-by-upi")
    public ResponseEntity<BigDecimal> getBalanceByUpi(@RequestParam String upiId) {
        BigDecimal balance = userService.getBalanceByUpi(upiId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @GetMapping("/update-balance-by-upi")
    public ResponseEntity<Void> updateBalanceByUpi(@RequestParam String upiId, @RequestParam BigDecimal newBalance) {
        userService.updateBalanceByUpi(upiId, newBalance);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/deposit")
    public ResponseEntity<String> depositMoney(@RequestParam String accountNo, @RequestParam BigDecimal amount) {
        String response = userService.depositMoney(accountNo, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawMoney(@RequestParam String accountNo, @RequestParam BigDecimal amount) {
        String response = userService.withdrawMoney(accountNo, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/verify-transaction-password")
    public ResponseEntity<Boolean> verifyTransactionPassword(@RequestParam String accountNo, @RequestParam String transactionPassword) {
        boolean isValid = userService.verifyTransactionPassword(accountNo, transactionPassword);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    @PostMapping("/verify-transaction-password-upi")
    public ResponseEntity<Boolean> verifyTransactionPasswordByUpi(@RequestParam String upiId, @RequestParam String transactionPassword) {
        boolean isValid = userService.verifyTransactionPasswordByUpi(upiId, transactionPassword);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }
    
    @GetMapping("/user-details")
    public ResponseEntity<UserDetailsDTO> getUserDetailsByUsername(@RequestParam String username) {
        Optional<AuthenticationModel> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            AuthenticationModel user = userOpt.get();
            UserDetailsDTO userDetails = new UserDetailsDTO(user.getUsername(), user.getEmail(), user.getAccountNo(), user.getBalance(), user.getUpiId());
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }  
}
