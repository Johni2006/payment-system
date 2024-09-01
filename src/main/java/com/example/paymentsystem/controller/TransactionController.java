package com.example.paymentsystem.controller;

import com.example.paymentsystem.model.TransactionHistory;
import com.example.paymentsystem.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @PostMapping("/create")
    public TransactionHistory createTransaction(@RequestBody TransactionHistory transaction) {
        return transactionHistoryService.createTransaction(transaction);
    }

    @GetMapping("/user/{userId}")
    public List<TransactionHistory> getUserTransactions(@PathVariable UUID userId) {
        return transactionHistoryService.getTransactionsByUser(userId);
    }
}
