package com.coforge.training.softbank.dashboard.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.coforge.training.softbank.dashboard.dto.TransactionSummaryDTO;
import com.coforge.training.softbank.dashboard.dto.UserDetailsDTO;
import com.coforge.training.softbank.dashboard.service.DashboardService;

@SpringBootTest
public class DashboardControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    private DashboardService dashboardService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DashboardController(dashboardService)).build();
    }

    @Test
    public void testGetTransactionHistory() throws Exception {
        TransactionSummaryDTO transaction1 = new TransactionSummaryDTO();
        transaction1.setId(1L);
        transaction1.setFromAccount("12345");
        transaction1.setToAccount("67890");
        transaction1.setFromUpiId("upi1");
        transaction1.setToUpiId("upi2");
        transaction1.setAmount(BigDecimal.valueOf(100));
        transaction1.setTransactionType("NEFT");
        transaction1.setRemarks("Test");
        transaction1.setTransactionDate(LocalDateTime.now());
        transaction1.setBalanceAfterTransaction(BigDecimal.valueOf(1000));

        TransactionSummaryDTO transaction2 = new TransactionSummaryDTO();
        transaction2.setId(2L);
        transaction2.setFromAccount("12345");
        transaction2.setToAccount("67890");
        transaction2.setFromUpiId("upi1");
        transaction2.setToUpiId("upi2");
        transaction2.setAmount(BigDecimal.valueOf(200));
        transaction2.setTransactionType("UPI");
        transaction2.setRemarks("Test");
        transaction2.setTransactionDate(LocalDateTime.now());
        transaction2.setBalanceAfterTransaction(BigDecimal.valueOf(1200));

        List<TransactionSummaryDTO> transactions = List.of(transaction1, transaction2);

        when(dashboardService.getTransactionHistory("12345", "upi1", "1month")).thenReturn(transactions);

        mockMvc.perform(get("/dashboard/transaction-history")
                .param("accountNo", "12345")
                .param("upiId", "upi1")
                .param("period", "1month"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'id':1,'fromAccount':'12345','toAccount':'67890','fromUpiId':'upi1','toUpiId':'upi2','amount':100,'transactionType':'NEFT','remarks':'Test','balanceAfterTransaction':1000},{'id':2,'fromAccount':'12345','toAccount':'67890','fromUpiId':'upi1','toUpiId':'upi2','amount':200,'transactionType':'UPI','remarks':'Test','balanceAfterTransaction':1200}]"));
    }

    @Test
    public void testGetTransactionHistoryCustom() throws Exception {
        TransactionSummaryDTO transaction1 = new TransactionSummaryDTO();
        transaction1.setId(1L);
        transaction1.setFromAccount("12345");
        transaction1.setToAccount("67890");
        transaction1.setFromUpiId("upi1");
        transaction1.setToUpiId("upi2");
        transaction1.setAmount(BigDecimal.valueOf(100));
        transaction1.setTransactionType("NEFT");
        transaction1.setRemarks("Test");
        transaction1.setTransactionDate(LocalDateTime.now());
        transaction1.setBalanceAfterTransaction(BigDecimal.valueOf(1000));

        TransactionSummaryDTO transaction2 = new TransactionSummaryDTO();
        transaction2.setId(2L);
        transaction2.setFromAccount("12345");
        transaction2.setToAccount("67890");
        transaction2.setFromUpiId("upi1");
        transaction2.setToUpiId("upi2");
        transaction2.setAmount(BigDecimal.valueOf(200));
        transaction2.setTransactionType("UPI");
        transaction2.setRemarks("Test");
        transaction2.setTransactionDate(LocalDateTime.now());
        transaction2.setBalanceAfterTransaction(BigDecimal.valueOf(1200));

        List<TransactionSummaryDTO> transactions = List.of(transaction1, transaction2);

        when(dashboardService.getTransactionHistoryCustom("12345", "upi1", "2024-01-01", "2024-12-31")).thenReturn(transactions);

        mockMvc.perform(get("/dashboard/transaction-history/custom")
                .param("accountNo", "12345")
                .param("upiId", "upi1")
                .param("startDate", "2024-01-01")
                .param("endDate", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'id':1,'fromAccount':'12345','toAccount':'67890','fromUpiId':'upi1','toUpiId':'upi2','amount':100,'transactionType':'NEFT','remarks':'Test','balanceAfterTransaction':1000},{'id':2,'fromAccount':'12345','toAccount':'67890','fromUpiId':'upi1','toUpiId':'upi2','amount':200,'transactionType':'UPI','remarks':'Test','balanceAfterTransaction':1200}]"));
    }

    @Test
    public void testGetTransactionHistoryCustom_NoTransactions() throws Exception {
        when(dashboardService.getTransactionHistoryCustom("12345", "upi1", "2024-01-01", "2024-12-31")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/dashboard/transaction-history/custom")
                .param("accountNo", "12345")
                .param("upiId", "upi1")
                .param("startDate", "2024-01-01")
                .param("endDate", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("No transactions found"));
    }

    @Test
    public void testGetUserDetails() throws Exception {
        UserDetailsDTO userDetails = new UserDetailsDTO();
        userDetails.setUsername("testuser");
        userDetails.setAccountNo("12345");
        userDetails.setUpiId("upi1");
        userDetails.setBalance(BigDecimal.valueOf(1000));

        when(dashboardService.getUserDetails("testuser")).thenReturn(userDetails);

        mockMvc.perform(get("/dashboard/user-details")
                .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'username':'testuser','accountNo':'12345','upiId':'upi1','balance':1000}"));
    }
}