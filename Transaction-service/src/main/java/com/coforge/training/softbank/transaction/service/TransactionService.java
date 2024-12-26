package com.coforge.training.softbank.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coforge.training.softbank.transaction.dto.NEFTTransactionDTO;
import com.coforge.training.softbank.transaction.dto.UPITransactionDTO;
import com.coforge.training.softbank.transaction.feign.AuthenticationFeignClient;
import com.coforge.training.softbank.transaction.model.NEFTTransaction;
import com.coforge.training.softbank.transaction.model.UPITransaction;
import com.coforge.training.softbank.transaction.repository.NEFTTransactionRepository;
import com.coforge.training.softbank.transaction.repository.UPITransactionRepository;

import jakarta.transaction.Transactional;

/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 11:53:55 am
* Project:Transaction-service
**/


@Service
public class TransactionService {

    private final NEFTTransactionRepository neftTransactionRepository;
    private final UPITransactionRepository upiTransactionRepository;
    private final AuthenticationFeignClient authClient;

    public TransactionService(NEFTTransactionRepository neftTransactionRepository, UPITransactionRepository upiTransactionRepository, AuthenticationFeignClient authClient) {
        this.neftTransactionRepository = neftTransactionRepository;
        this.upiTransactionRepository = upiTransactionRepository;
        this.authClient = authClient;
    }

    @Transactional
    public String performNeftTransaction(NEFTTransactionDTO transactionDTO) {
        // Verify transaction password
        if (!authClient.verifyTransactionPassword(transactionDTO.getFromAccount(), transactionDTO.getTransactionPassword())) {
            return "Invalid transaction password.";
        }

        // Verify sender account
        if (!authClient.verifyAccount(transactionDTO.getFromAccount())) {
            return "Sender account number not found.";
        }
        // Verify receiver account
        if (!authClient.verifyAccount(transactionDTO.getToAccount())) {
            return "Receiver account number not found.";
        }

        // Fetch balances
        BigDecimal fromAccountBalance = authClient.getBalance(transactionDTO.getFromAccount());
        BigDecimal toAccountBalance = authClient.getBalance(transactionDTO.getToAccount());

        // Check sufficient balance
        if (fromAccountBalance.compareTo(transactionDTO.getAmount()) < 0) {
            return "Insufficient balance.";
        }

        // Perform transaction
        fromAccountBalance = fromAccountBalance.subtract(transactionDTO.getAmount());
        toAccountBalance = toAccountBalance.add(transactionDTO.getAmount());

        // Update balances
        authClient.updateBalance(transactionDTO.getFromAccount(), fromAccountBalance);
        authClient.updateBalance(transactionDTO.getToAccount(), toAccountBalance);

        // Save transaction
        NEFTTransaction transaction = new NEFTTransaction();
        transaction.setFromAccount(transactionDTO.getFromAccount());
        transaction.setToAccount(transactionDTO.getToAccount());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionType("NEFT"); // Ensure transaction type is set
        transaction.setRemarks(transactionDTO.getRemarks());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setFromAccountBalanceAfterTransaction(fromAccountBalance);
        transaction.setToAccountBalanceAfterTransaction(toAccountBalance);
        neftTransactionRepository.save(transaction);

        return "Transaction successful.";
    }
    @Transactional
    public String performUpiTransaction(UPITransactionDTO transactionDTO) {
        // Verify transaction password
        if (!authClient.verifyTransactionPasswordByUpi(transactionDTO.getFromUpiId(), transactionDTO.getTransactionPassword())) {
            return "Invalid transaction password.";
        }

        // Verify sender UPI ID
        if (!authClient.verifyUpi(transactionDTO.getFromUpiId())) {
            return "Sender UPI ID not found.";
        }
        // Verify receiver UPI ID
        if (!authClient.verifyUpi(transactionDTO.getToUpiId())) {
            return "Receiver UPI ID not found.";
        }

        // Fetch balances
        BigDecimal fromAccountBalance = authClient.getBalanceByUpi(transactionDTO.getFromUpiId());
        BigDecimal toAccountBalance = authClient.getBalanceByUpi(transactionDTO.getToUpiId());

        // Check sufficient balance
        if (fromAccountBalance.compareTo(transactionDTO.getAmount()) < 0) {
            return "Insufficient balance.";
        }

        // Perform transaction
        fromAccountBalance = fromAccountBalance.subtract(transactionDTO.getAmount());
        toAccountBalance = toAccountBalance.add(transactionDTO.getAmount());

        // Update balances
        authClient.updateBalanceByUpi(transactionDTO.getFromUpiId(), fromAccountBalance);
        authClient.updateBalanceByUpi(transactionDTO.getToUpiId(), toAccountBalance);

        // Save transaction
        UPITransaction transaction = new UPITransaction();
        transaction.setFromUpiId(transactionDTO.getFromUpiId());
        transaction.setToUpiId(transactionDTO.getToUpiId());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setRemarks(transactionDTO.getRemarks());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setFromAccountBalanceAfterTransaction(fromAccountBalance);
        transaction.setToAccountBalanceAfterTransaction(toAccountBalance);
        upiTransactionRepository.save(transaction);

        return "Transaction successful.";
    }

    public List<NEFTTransaction> getNeftTransactionHistory(String accountNo) {
        return neftTransactionRepository.findByFromAccount(accountNo);
    }

    public List<UPITransaction> getUpiTransactionHistory(String upiId) {
        return upiTransactionRepository.findByFromUpiId(upiId);
    }

    public List<NEFTTransaction> getNeftTransactionHistory(String accountNo, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<NEFTTransaction> transactions = neftTransactionRepository.findByFromAccountOrToAccountAndTransactionDateBetween(accountNo, accountNo, start.atStartOfDay(), end.atTime(23, 59, 59));
        transactions.forEach(transaction -> {
            if (transaction.getFromAccount().equals(accountNo)) {
                transaction.setAmount(transaction.getAmount().negate()); // Debit
            } else {
                transaction.setAmount(transaction.getAmount()); // Credit
            }
        });
        return transactions;
    }

    public List<UPITransaction> getUpiTransactionHistory(String upiId, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<UPITransaction> transactions = upiTransactionRepository.findByFromUpiIdOrToUpiIdAndTransactionDateBetween(upiId, upiId, start.atStartOfDay(), end.atTime(23, 59, 59));
        transactions.forEach(transaction -> {
            if (transaction.getFromUpiId().equals(upiId)) {
                transaction.setAmount(transaction.getAmount().negate()); // Debit
            } else {
                transaction.setAmount(transaction.getAmount()); // Credit
            }
        });
        return transactions;
    }
}