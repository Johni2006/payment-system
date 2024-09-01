package com.example.paymentsystem.service.impl;

import com.example.paymentsystem.model.TransactionHistory;
import com.example.paymentsystem.repository.TransactionHistoryRepository;
import com.example.paymentsystem.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Override
    public TransactionHistory createTransaction(TransactionHistory transaction) {
        return transactionHistoryRepository.save(transaction);
    }

    @Override
    public List<TransactionHistory> getTransactionsByUser(UUID userId) {
        return transactionHistoryRepository.findByUserId(userId);
    }
}
