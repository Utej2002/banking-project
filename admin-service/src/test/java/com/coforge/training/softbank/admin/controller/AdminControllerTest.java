package com.coforge.training.softbank.admin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.coforge.training.softbank.admin.dto.AccountRequestDTO;
import com.coforge.training.softbank.admin.feign.AuthenticationFeignClient;
import com.coforge.training.softbank.admin.feign.UserRegistrationFeignClient;
import com.coforge.training.softbank.admin.service.AdminService;

@SpringBootTest
class AdminControllerTest {

	@MockitoBean
    private AdminService adminService;

    @MockitoBean
    private UserRegistrationFeignClient userRegistrationFeignClient;

    @MockitoBean
    private AuthenticationFeignClient authenticationFeignClient;

    @Autowired
    private AdminController adminController;

    private MockMvc mockMvc;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(post("/admin/login")
                .param("username", "admin")
                .param("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));

        mockMvc.perform(post("/admin/login")
                .param("username", "wrong")
                .param("password", "wrong"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    public void testGetPendingAccountRequests() throws Exception {
        when(adminService.getPendingAccountRequests()).thenReturn(Collections.singletonList(new AccountRequestDTO()));

        mockMvc.perform(get("/admin/account-requests")
                .param("username", "admin")
                .param("password", "admin"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/account-requests")
                .param("username", "wrong")
                .param("password", "wrong"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testApproveAccount() throws Exception {
        when(adminService.approveAccount(anyString())).thenReturn("Account approved");

        mockMvc.perform(put("/admin/account-requests/approve/1234567890")
                .param("username", "admin")
                .param("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account approved"));

        mockMvc.perform(put("/admin/account-requests/approve/1234567890")
                .param("username", "wrong")
                .param("password", "wrong"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));
    }

    @Test
    public void testRejectAccount() throws Exception {
        when(adminService.rejectAccount(anyString())).thenReturn("Account rejected");

        mockMvc.perform(put("/admin/account-requests/reject/1234567890")
                .param("username", "admin")
                .param("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account rejected"));

        mockMvc.perform(put("/admin/account-requests/reject/1234567890")
                .param("username", "wrong")
                .param("password", "wrong"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));
    }

    @Test
    public void testDepositMoney() throws Exception {
        when(adminService.depositMoney(anyString(), any(BigDecimal.class))).thenReturn("Deposit successful");

        mockMvc.perform(post("/admin/accounts/deposit")
                .param("accountNo", "1234567890")
                .param("amount", "1000")
                .param("username", "admin")
                .param("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));

        mockMvc.perform(post("/admin/accounts/deposit")
                .param("accountNo", "1234567890")
                .param("amount", "1000")
                .param("username", "wrong")
                .param("password", "wrong"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));
    }

    @Test
    public void testWithdrawMoney() throws Exception {
        when(adminService.withdrawMoney(anyString(), any(BigDecimal.class))).thenReturn("Withdrawal successful");

        mockMvc.perform(post("/admin/accounts/withdraw")
                .param("accountNo", "1234567890")
                .param("amount", "500")
                .param("username", "admin")
                .param("password", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful"));

        mockMvc.perform(post("/admin/accounts/withdraw")
                .param("accountNo", "1234567890")
                .param("amount", "500")
                .param("username", "wrong")
                .param("password", "wrong"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));
    }
}