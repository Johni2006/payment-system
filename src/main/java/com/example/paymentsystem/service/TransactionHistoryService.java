package com.example.paymentsystem.service;

import com.example.paymentsystem.model.TransactionHistory;
import java.util.List;
import java.util.UUID;

public interface TransactionHistoryService {
    TransactionHistory createTransaction(TransactionHistory transaction);
    List<TransactionHistory> getTransactionsByUser(UUID userId);
}
