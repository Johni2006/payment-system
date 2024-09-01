package com.example.paymentsystem.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(name = "telegram_token")
    private String telegramToken;

    @Column(precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "secret_key", unique = true) // Исправлено имя столбца
    private String secretKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_group", nullable = false, length = 50)
    private UserGroup userGroup;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "wallet_address")
    private String walletAddress;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column(name = "total_transactions", nullable = false)
    private int totalTransactions = 0;

    @Column(name = "completed_transactions", nullable = false)
    private int completedTransactions = 0;

    @Column(name = "cancelled_transactions", nullable = false)
    private int cancelledTransactions = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<ConfirmationToken> confirmationTokens;

    // Getters и Setters

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

    public UserGroup getUserGroup() { // Исправлено имя метода
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) { // Исправлено имя метода
        this.userGroup = userGroup;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public int getCompletedTransactions() {
        return completedTransactions;
    }

    public void setCompletedTransactions(int completedTransactions) {
        this.completedTransactions = completedTransactions;
    }

    public int getCancelledTransactions() {
        return cancelledTransactions;
    }

    public void setCancelledTransactions(int cancelledTransactions) {
        this.cancelledTransactions = cancelledTransactions;
    }
}
