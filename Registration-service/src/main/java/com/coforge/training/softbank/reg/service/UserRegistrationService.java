package com.coforge.training.softbank.reg.service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.coforge.training.softbank.reg.feign.AuthServiceFeignClient;
import com.coforge.training.softbank.reg.model.AccountRequest;
import com.coforge.training.softbank.reg.model.UserProfile;
import com.coforge.training.softbank.reg.repository.AccountRequestRepository;
import com.coforge.training.softbank.reg.repository.UserProfileRepository;


/**
* User   : Singuluri.Kumar
* Date   : 14-Dec-2024
* Time   : 8:50:26 pm
* Project:registration-service
**/

@Service
public class UserRegistrationService {
 
    private final UserProfileRepository userProfileRepository;
    private final AccountRequestRepository accountRequestRepository;
    private final AuthServiceFeignClient authServiceFeignClient;
 
    public UserRegistrationService(UserProfileRepository userProfileRepository,
                                   AccountRequestRepository accountRequestRepository,
                                   AuthServiceFeignClient authServiceFeignClient) {
        this.userProfileRepository = userProfileRepository;
        this.accountRequestRepository = accountRequestRepository;
        this.authServiceFeignClient = authServiceFeignClient;
    }
 
    public String createAccountRequest(UserProfile userProfile, String accountType, byte[] aadharPdf, byte[] passportPhoto) {
        // Save user profile
        userProfile.setAadharPdf(aadharPdf);
        userProfile.setPassportPhoto(passportPhoto);
        UserProfile savedProfile = userProfileRepository.save(userProfile);

        // Create account request and link it to the saved user profile
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserProfile(savedProfile); // Set the relationship
        accountRequest.setAccountType(accountType);
        accountRequest.setAccountNo(generateAccountNo());
        accountRequest.setAadharPdf(aadharPdf);
        accountRequest.setPassportPhoto(passportPhoto);

        savedProfile.setAccountRequest(accountRequest); // Update the reverse relationship

        // Save account request
        accountRequestRepository.save(accountRequest);

        return "Account request created successfully. Account No: " + accountRequest.getAccountNo();
    }

 
    public String registerForInternetBanking(String accountNo, String username, String loginPassword,
                                             String transactionPassword, String email, String authPin) {
        // Check if account exists
        AccountRequest accountRequest = accountRequestRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Account does not exist"));
 
        // Check if account is approved
        if (!"Approved".equalsIgnoreCase(accountRequest.getStatus())) {
            throw new ResponseStatusException(BAD_REQUEST, "Account is not approved for internet banking");
        }
 
        // Call Authentication Service to register user
        AuthServiceFeignClient.AuthUserRequest authUserRequest = new AuthServiceFeignClient.AuthUserRequest();
        authUserRequest.setUsername(username);
        authUserRequest.setEmail(email);
        authUserRequest.setAccountNo(accountNo);
        authUserRequest.setAuthPin(authPin);
        authUserRequest.setLoginPassword(loginPassword);
        authUserRequest.setTransactionPassword(transactionPassword);
 
        return authServiceFeignClient.registerUser(authUserRequest);
    }
 
    private String generateAccountNo() {
        return "SB" + System.currentTimeMillis(); // Simple account number generator
    }
    
    public boolean isAccountApproved(String accountNo) {
        // Find account by accountNo
        AccountRequest accountRequest = accountRequestRepository.findByAccountNo(accountNo).orElse(null);
 
        // If account does not exist or is not approved, return false
        return accountRequest != null && "Approved".equals(accountRequest.getStatus());
    }
    
    // Fetch all pending account requests
    public List<AccountRequest> getPendingAccountRequests() {
        return accountRequestRepository.findByStatus("Pending");
    }
 
    public boolean approveAccount(String accountNo) {
        AccountRequest accountRequest = accountRequestRepository.findByAccountNo(accountNo).orElse(null);
        if (accountRequest == null) {
            return false;
        }
        accountRequest.setStatus("Approved");
        accountRequest.setApprovedAt(java.time.LocalDateTime.now());
        accountRequestRepository.save(accountRequest);
        return true;
    }
 
    public boolean rejectAccount(String accountNo) {
        AccountRequest accountRequest = accountRequestRepository.findByAccountNo(accountNo).orElse(null);
        if (accountRequest == null) {
            return false;
        }
        accountRequest.setStatus("Rejected");
        accountRequestRepository.save(accountRequest);
        return true;
    }
    
    public byte[] getPassportPhotoByAccountNo(String accountNo) {
        AccountRequest accountRequest = accountRequestRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return accountRequest.getPassportPhoto();
    }

}