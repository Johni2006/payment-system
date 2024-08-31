package com.example.paymentsystem.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    private String username;

    private String telegramToken;

    private BigDecimal balance = BigDecimal.ZERO;

    private String secretKey;

    @Enumerated(EnumType.STRING)
    private UserGroup group;

    private boolean isActive = false;

    private String walletAddress;

    @Column(nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    private int transactionCount = 0;

    private int completedTransactionCount = 0;

    private int cancelledTransactionCount = 0;

    // Геттеры и сеттеры
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelegramToken() {
        return telegramToken;
    }

    public void setTelegramToken(String telegramToken) {
        this.telegramToken = telegramToken;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public int getCompletedTransactionCount() {
        return completedTransactionCount;
    }

    public void setCompletedTransactionCount(int completedTransactionCount) {
        this.completedTransactionCount = completedTransactionCount;
    }

    public int getCancelledTransactionCount() {
        return cancelledTransactionCount;
    }

    public void setCancelledTransactionCount(int cancelledTransactionCount) {
        this.cancelledTransactionCount = cancelledTransactionCount;
    }
}
