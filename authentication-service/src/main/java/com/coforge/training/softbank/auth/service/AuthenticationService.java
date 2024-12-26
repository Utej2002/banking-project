package com.coforge.training.softbank.auth.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coforge.training.softbank.auth.model.AuthenticationModel;
import com.coforge.training.softbank.auth.repository.AuthenticationRepository;

@Service
public class AuthenticationService {

    @Autowired
    private final AuthenticationRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    public AuthenticationService(AuthenticationRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationModel registerUser(String username, String email, String accountNo, String authPin,
                                            String loginPassword, String transactionPassword) {
        if (!isValidPassword(loginPassword) || !isValidPassword(transactionPassword)) {
            throw new IllegalArgumentException("Password does not meet security requirements.");
        }
        if (userRepository.findByAccountNo(accountNo).isPresent()) {
            throw new IllegalArgumentException("Account already registered.");
        }
        AuthenticationModel user = new AuthenticationModel();
        user.setUsername(username);
        user.setEmail(email);
        user.setAccountNo(accountNo);
        user.setAuthPin(authPin);
        user.setLoginPassword(passwordEncoder.encode(loginPassword));
        user.setTransactionPassword(passwordEncoder.encode(transactionPassword));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpiId(username + "@softbank"); // Set the UPI ID
        user.setBalance(BigDecimal.valueOf(1000));
        return userRepository.save(user);
    }

    public Optional<AuthenticationModel> findByAccountNo(String accountNo) {
        return userRepository.findByAccountNo(accountNo);
    }

    public Optional<AuthenticationModel> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String getLoginPasswordByAuthPin(String authPin) {
        return userRepository.findByAuthPin(authPin)
                .map(user -> passwordEncoder.encode(user.getLoginPassword()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid Auth Pin."));
    }

    private boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    public void resetLoginPassword(String authPin, String newPassword) {
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Password does not meet security requirements.");
        }
        Optional<AuthenticationModel> optionalUser = userRepository.findByAuthPin(authPin);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }
        AuthenticationModel user = optionalUser.get();
        user.setLoginPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
 
public void resetTransactionPassword(String authPin, String newPassword) {
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Password does not meet security requirements.");
        }
        Optional<AuthenticationModel> optionalUser = userRepository.findByAuthPin(authPin);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }
        AuthenticationModel user = optionalUser.get();
        user.setTransactionPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
 
 
    
    
    
    //new 
    
    public String getUsernameByAuthPin(String authPin) {
        return userRepository.findByAuthPin(authPin)
                .map(AuthenticationModel::getUsername)
                .orElseThrow(() -> new IllegalArgumentException("Auth Pin not found."));
    }

    
   

    public void lockUser(AuthenticationModel user) {
        user.setLocked(true);
        userRepository.save(user);
    }

    public void resetFailedAttempts(AuthenticationModel user) {
        user.setFailedAttempts(0);
        userRepository.save(user);
    }

    public void incrementFailedAttempts(AuthenticationModel user) {
        user.setFailedAttempts(user.getFailedAttempts() + 1);
        if (user.getFailedAttempts() >= 3) {
            lockUser(user);
        } else {
            userRepository.save(user);
        }
    }

    public boolean verifyLoginPassword(AuthenticationModel user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getLoginPassword());
    }

    public boolean verifyTransactionPassword(AuthenticationModel user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getTransactionPassword());
    }
    
    public BigDecimal getBalance(String accountNo) {
        AuthenticationModel user = userRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        return user.getBalance();
    }

    public void updateBalance(String accountNo, BigDecimal newBalance) {
        AuthenticationModel user = userRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        user.setBalance(newBalance);
        userRepository.save(user);
    }

    public boolean verifyAccount(String accountNo) {
        return userRepository.findByAccountNo(accountNo).isPresent();
    }

    public boolean verifyUpi(String upiId) {
        return userRepository.findByUpiId(upiId).isPresent();
    }
    
    public BigDecimal getBalanceByUpi(String upiId) {
        AuthenticationModel user = userRepository.findByUpiId(upiId)
                .orElseThrow(() -> new IllegalArgumentException("UPI ID not found."));
        return user.getBalance();
    }

    public void updateBalanceByUpi(String upiId, BigDecimal newBalance) {
        AuthenticationModel user = userRepository.findByUpiId(upiId)
                .orElseThrow(() -> new IllegalArgumentException("UPI ID not found."));
        user.setBalance(newBalance);
        userRepository.save(user);
    }
    public String depositMoney(String accountNo, BigDecimal amount) {
        Optional<AuthenticationModel> userOpt = userRepository.findByAccountNo(accountNo);
        if (userOpt.isEmpty()) {
            return "Account number is not present.";
        }
        AuthenticationModel user = userOpt.get();
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
        return "Deposit successful.";
    }

    public String withdrawMoney(String accountNo, BigDecimal amount) {
        Optional<AuthenticationModel> userOpt = userRepository.findByAccountNo(accountNo);
        if (userOpt.isEmpty()) {
            return "Account number is not present.";
        }
        AuthenticationModel user = userOpt.get();
        if (user.getBalance().compareTo(amount) < 0) {
            return "Insufficient balance.";
        }
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);
        return "Withdrawal successful.";
    }
    
    public boolean verifyTransactionPassword(String accountNo, String rawPassword) {
        AuthenticationModel user = userRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new IllegalArgumentException("Account number not found."));
        return passwordEncoder.matches(rawPassword, user.getTransactionPassword());
    }

    public boolean verifyTransactionPasswordByUpi(String upiId, String rawPassword) {
        AuthenticationModel user = userRepository.findByUpiId(upiId)
                .orElseThrow(() -> new IllegalArgumentException("UPI ID not found."));
        return passwordEncoder.matches(rawPassword, user.getTransactionPassword());
    }
}