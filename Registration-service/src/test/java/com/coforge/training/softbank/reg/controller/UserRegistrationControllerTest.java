package com.coforge.training.softbank.reg.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.coforge.training.softbank.reg.dto.AccountRequestDTO;
import com.coforge.training.softbank.reg.model.AccountRequest;
import com.coforge.training.softbank.reg.model.UserProfile;
import com.coforge.training.softbank.reg.service.UserRegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class UserRegistrationControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    private UserRegistrationService registrationService;

    @Autowired
    private UserRegistrationController registrationController;

    private UserProfile userProfile;
    
    private AccountRequest accountRequest;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        userProfile = new UserProfile();
        userProfile.setEmail("test@example.com");
        userProfile.setName("Test User");
        userProfile.setMobileNumber("1234567890");
        userProfile.setResidentialAddress("123 Test St");
        userProfile.setPermanentAddress("456 Test Ave");
        userProfile.setAadharCard("1234-5678-9012");

        accountRequest = new AccountRequest();
        accountRequest.setId(1L);
        accountRequest.setAccountNo("SB1234567890");
        accountRequest.setAccountType("Savings");
        accountRequest.setStatus("Pending");
        accountRequest.setCreatedAt(LocalDateTime.now());
        accountRequest.setApprovedAt(null);
        accountRequest.setAadharPdf(new byte[]{1, 2, 3});
        accountRequest.setPassportPhoto(new byte[]{4, 5, 6});
        accountRequest.setUserProfile(userProfile);
    }

    @AfterEach
    void tearDown() throws Exception {
        userProfile = null;
        accountRequest = null;
    }

    @Test
    void testUserRegistrationController() {
        assertNotNull(registrationController);
    }

    @Test
    void testCreateAccountRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userProfileJson = objectMapper.writeValueAsString(userProfile);

        MockMultipartFile userProfileFile = new MockMultipartFile("userProfile", "", "application/json", userProfileJson.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile aadharPdf = new MockMultipartFile("aadharPdf", "aadhar.pdf", "application/pdf", "dummy pdf content".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile passportPhoto = new MockMultipartFile("passportPhoto", "photo.jpg", "image/jpeg", "dummy image content".getBytes(StandardCharsets.UTF_8));

        when(registrationService.createAccountRequest(any(UserProfile.class), anyString(), any(byte[].class), any(byte[].class)))
                .thenReturn("Account request created successfully");

        ResponseEntity<String> response = registrationController.createAccountRequest(userProfileJson, "Savings", aadharPdf, passportPhoto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Account request created successfully", response.getBody());

        verify(registrationService, times(1)).createAccountRequest(any(UserProfile.class), anyString(), any(byte[].class), any(byte[].class));
    }

    @Test
    void testVerifyAccount() {
        when(registrationService.isAccountApproved(anyString())).thenReturn(true);

        ResponseEntity<Boolean> response = registrationController.verifyAccount("123456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());

        verify(registrationService, times(1)).isAccountApproved("123456");
    }

    @Test
    void testGetPendingAccountRequests() {
        List<AccountRequest> mockPendingRequests = Collections.emptyList();

        when(registrationService.getPendingAccountRequests()).thenReturn(mockPendingRequests);

        ResponseEntity<List<AccountRequestDTO>> response = registrationController.getPendingAccountRequests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPendingRequests, response.getBody());

        verify(registrationService, times(1)).getPendingAccountRequests();
    }

    @Test
    void testApproveAccount() {
        when(registrationService.approveAccount(anyString())).thenReturn(true);

        ResponseEntity<String> response = registrationController.approveAccount("123456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account approved successfully", response.getBody());

        verify(registrationService, times(1)).approveAccount("123456");
    }

    @Test
    void testRejectAccount() {
        when(registrationService.rejectAccount(anyString())).thenReturn(true);

        ResponseEntity<String> response = registrationController.rejectAccount("123456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account rejected successfully", response.getBody());

        verify(registrationService, times(1)).rejectAccount("123456");
    }

    @Test
    void testConvertToDTO() {
        AccountRequestDTO dto = registrationController.convertToDTO(accountRequest);

        assertNotNull(dto);
        assertEquals(accountRequest.getId(), dto.getId());
        assertEquals(accountRequest.getAccountNo(), dto.getAccountNo());
        assertEquals(accountRequest.getAccountType(), dto.getAccountType());
        assertEquals(accountRequest.getStatus(), dto.getStatus());
        assertEquals(accountRequest.getCreatedAt(), dto.getCreatedAt());
        assertEquals(accountRequest.getApprovedAt(), dto.getApprovedAt());
        assertArrayEquals(accountRequest.getAadharPdf(), dto.getAadharPdf());
        assertArrayEquals(accountRequest.getPassportPhoto(), dto.getPassportPhoto());
    }
}