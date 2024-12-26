package com.coforge.training.softbank.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.coforge.training.softbank.auth.feign.UserRegistrationServiceFeignClient;
import com.coforge.training.softbank.auth.model.AuthenticationModel;
import com.coforge.training.softbank.auth.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class AuthenticationControllerTest {

	 @MockitoBean
	    private AuthenticationService userService;

	 @MockitoBean
	    private UserRegistrationServiceFeignClient userRegistrationServiceFeignClient;

	    @Autowired
	    private AuthenticationController authenticationController;

	    private MockMvc mockMvc;

	    @BeforeEach
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
	    }

	    @Test
	    public void testRegisterUser() throws Exception {
	        Map<String, String> request = new HashMap<>();
	        request.put("username", "testuser");
	        request.put("email", "testuser@example.com");
	        request.put("accountNo", "1234567890");
	        request.put("authPin", "1234");
	        request.put("loginPassword", "password");
	        request.put("transactionPassword", "transpassword");

	        when(userRegistrationServiceFeignClient.verifyAccount(anyString())).thenReturn(true);

	        mockMvc.perform(post("/auth/register")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(request)))
	                .andExpect(status().isCreated());

	        // Test for account not approved
	        when(userRegistrationServiceFeignClient.verifyAccount(anyString())).thenReturn(false);

	        mockMvc.perform(post("/auth/register")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(request)))
	                .andExpect(status().isBadRequest())
	                .andExpect(result -> assertEquals("Account number is not approved or does not exist.", result.getResponse().getContentAsString()));
	    }

	 
	    @Test
	    public void testForgotUsername() throws Exception {
	        Map<String, String> request = new HashMap<>();
	        request.put("authPin", "1234");

	        when(userService.getUsernameByAuthPin(anyString())).thenReturn("testuser");

	        mockMvc.perform(post("/auth/forgot-username")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(request)))
	                .andExpect(status().isOk());

	        // Test for missing authPin
	        request.remove("authPin");

	        mockMvc.perform(post("/auth/forgot-username")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(request)))
	                .andExpect(status().isBadRequest())
	                .andExpect(result -> assertEquals("Auth Pin is required.", result.getResponse().getContentAsString()));
	    }

	    @Test
	    public void testLogin() throws Exception {
	        Map<String, String> request = new HashMap<>();
	        request.put("username", "testuser");
	        request.put("password", "password");

	        AuthenticationModel user = new AuthenticationModel();
	        user.setUsername("testuser");
	        user.setLoginPassword("password");

	        when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));
	        when(userService.verifyLoginPassword(user, "password")).thenReturn(true);

	        mockMvc.perform(post("/auth/login")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(request)))
	                .andExpect(status().isOk());

	        // Test for invalid credentials
	        when(userService.verifyLoginPassword(user, "password")).thenReturn(false);

	        mockMvc.perform(post("/auth/login")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(request)))
	                .andExpect(status().isUnauthorized())
	                .andExpect(result -> assertEquals("Invalid credentials", result.getResponse().getContentAsString()));
	    }

	    @Test
	    public void testResetLoginPassword() throws Exception {
	        Map<String, String> request = new HashMap<>();
	        request.put("authPin", "1234");
	        request.put("newPassword", "newpassword");

	        mockMvc.perform(post("/auth/reset-login-password")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(request)))
	                .andExpect(status().isOk());

	        // Test for missing parameters
	        request.remove("authPin");

	        mockMvc.perform(post("/auth/reset-login-password")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(request)))
	                .andExpect(status().isBadRequest())
	                .andExpect(result -> assertEquals("Required parameters 'authPin' and 'newPassword' are not present.", result.getResponse().getContentAsString()));
	    }

	    @Test
	    public void testResetTransactionPassword() throws Exception {
	        Map<String, String> request = new HashMap<>();
	        request.put("authPin", "1234");
	        request.put("newPassword", "newpassword");

	        mockMvc.perform(post("/auth/reset-transaction-password")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(request)))
	                .andExpect(status().isOk());

	        // Test for missing parameters
	        request.remove("authPin");

	        mockMvc.perform(post("/auth/reset-transaction-password")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(new ObjectMapper().writeValueAsString(request)))
	                .andExpect(status().isBadRequest())
	                .andExpect(result -> assertEquals("Required parameters 'authPin' and 'newPassword' are not present.", result.getResponse().getContentAsString()));
	    }

	    @Test
	    public void testGetBalance() throws Exception {
	        when(userService.getBalance(anyString())).thenReturn(BigDecimal.valueOf(1000));

	        mockMvc.perform(get("/auth/balance")
	                .param("accountNo", "1234567890"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testUpdateBalance() throws Exception {
	        mockMvc.perform(get("/auth/update-balance")
	                .param("accountNo", "1234567890")
	                .param("newBalance", "2000"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testVerifyAccount() throws Exception {
	        when(userService.verifyAccount(anyString())).thenReturn(true);

	        mockMvc.perform(get("/auth/verify-account")
	                .param("accountNo", "1234567890"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testVerifyUpi() throws Exception {
	        when(userService.verifyUpi(anyString())).thenReturn(true);

	        mockMvc.perform(get("/auth/verify-upi")
	                .param("upiId", "test@upi"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testGetBalanceByUsername() throws Exception {
	        AuthenticationModel user = new AuthenticationModel();
	        user.setUsername("testuser");
	        user.setBalance(BigDecimal.valueOf(1000));

	        when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));

	        mockMvc.perform(get("/auth/balance/testuser"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testGetBalanceByUpi() throws Exception {
	        when(userService.getBalanceByUpi(anyString())).thenReturn(BigDecimal.valueOf(1000));

	        mockMvc.perform(get("/auth/balance-by-upi")
	                .param("upiId", "test@upi"))
	                .andExpect(status().isOk());
	    }
	   
	    @Test
	    public void testUpdateBalanceByUpi() throws Exception {
	        mockMvc.perform(get("/auth/update-balance-by-upi")
	                .param("upiId", "test@upi")
	                .param("newBalance", "2000"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testDepositMoney() throws Exception {
	        when(userService.depositMoney(anyString(), any(BigDecimal.class))).thenReturn("Deposit successful");

	        mockMvc.perform(post("/auth/deposit")
	                .param("accountNo", "1234567890")
	                .param("amount", "1000"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testWithdrawMoney() throws Exception {
	        when(userService.withdrawMoney(anyString(), any(BigDecimal.class))).thenReturn("Withdrawal successful");

	        mockMvc.perform(post("/auth/withdraw")
	                .param("accountNo", "1234567890")
	                .param("amount", "500"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testVerifyTransactionPassword() throws Exception {
	        when(userService.verifyTransactionPassword(anyString(), anyString())).thenReturn(true);

	        mockMvc.perform(post("/auth/verify-transaction-password")
	                .param("accountNo", "1234567890")
	                .param("transactionPassword", "transpassword"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testVerifyTransactionPasswordByUpi() throws Exception {
	        when(userService.verifyTransactionPasswordByUpi(anyString(), anyString())).thenReturn(true);

	        mockMvc.perform(post("/auth/verify-transaction-password-upi")
	                .param("upiId", "test@upi")
	                .param("transactionPassword", "transpassword"))
	                .andExpect(status().isOk());
	    }

	    @Test
	    public void testGetUserDetailsByUsername() throws Exception {
	        AuthenticationModel user = new AuthenticationModel();
	        user.setUsername("testuser");
	        user.setEmail("testuser@example.com");
	        user.setAccountNo("1234567890");
	        user.setBalance(BigDecimal.valueOf(1000));
	        user.setUpiId("test@upi");

	        when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));

	        mockMvc.perform(get("/auth/user-details")
	                .param("username", "testuser"))
	                .andExpect(status().isOk());
	    }
	
}