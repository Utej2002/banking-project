package com.coforge.training.softbank.reg.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.coforge.training.softbank.reg.dto.AccountRequestDTO;
import com.coforge.training.softbank.reg.model.AccountRequest;
import com.coforge.training.softbank.reg.model.UserProfile;
import com.coforge.training.softbank.reg.service.UserRegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
* User   : Singuluri.Kumar
* Date   : 14-Dec-2024
* Time   : 8:58:36 pm
* Project:registration-service
**/

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/registration")
public class UserRegistrationController {
 
    private final UserRegistrationService registrationService;
 
    public UserRegistrationController(UserRegistrationService registrationService) {
        this.registrationService = registrationService;
    }
 
   

    @PostMapping("/create-account")
    public ResponseEntity<String> createAccountRequest(@RequestParam("userProfile") String userProfileJson,
                                                       @RequestParam("accountType") String accountType,
                                                       @RequestParam("aadharPdf") MultipartFile aadharPdf,
                                                       @RequestParam("passportPhoto") MultipartFile passportPhoto) {
        try {
            // Convert userProfileJson to UserProfile object
            ObjectMapper objectMapper = new ObjectMapper();
            UserProfile userProfile = objectMapper.readValue(userProfileJson, UserProfile.class);

            // Convert MultipartFile to byte[]
            byte[] aadharPdfBytes = aadharPdf.getBytes();
            byte[] passportPhotoBytes = passportPhoto.getBytes();

            String response = registrationService.createAccountRequest(userProfile, accountType, aadharPdfBytes, passportPhotoBytes);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Error processing request", HttpStatus.BAD_REQUEST);
        }
    }

    
    @GetMapping("/verify-account")
    public ResponseEntity<Boolean> verifyAccount(@RequestParam String accountNo) {
        boolean isApproved = registrationService.isAccountApproved(accountNo);
        return new ResponseEntity<>(isApproved, HttpStatus.OK);
    }
    
    @GetMapping("/account-requests")
    public ResponseEntity<List<AccountRequestDTO>> getPendingAccountRequests() {
        List<AccountRequest> pendingRequests = registrationService.getPendingAccountRequests();
        List<AccountRequestDTO> response = pendingRequests.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
 
    @PutMapping("/account-requests/approve/{accountNo}")
    public ResponseEntity<String> approveAccount(@PathVariable String accountNo) {
        boolean success = registrationService.approveAccount(accountNo);
        if (success) {
            return new ResponseEntity<>("Account approved successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
    }
 
    @PutMapping("/account-requests/reject/{accountNo}")
    public ResponseEntity<String> rejectAccount(@PathVariable String accountNo) {
        boolean success = registrationService.rejectAccount(accountNo);
        if (success) {
            return new ResponseEntity<>("Account rejected successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
    }
 
    public AccountRequestDTO convertToDTO(AccountRequest accountRequest) {
        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setId(accountRequest.getId());
        dto.setAccountNo(accountRequest.getAccountNo());
        dto.setAccountType(accountRequest.getAccountType());
        dto.setStatus(accountRequest.getStatus());
        dto.setCreatedAt(accountRequest.getCreatedAt());
        dto.setApprovedAt(accountRequest.getApprovedAt());
        dto.setAadharPdf(accountRequest.getAadharPdf());
        dto.setPassportPhoto(accountRequest.getPassportPhoto());
        return dto;
    }
    
    @GetMapping("/account-requests/photo/{accountNo}")
    public ResponseEntity<byte[]> getPassportPhoto(@PathVariable String accountNo) {
        byte[] photo = registrationService.getPassportPhotoByAccountNo(accountNo);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photo);
    }
}




