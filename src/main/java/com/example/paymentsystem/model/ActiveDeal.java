package com.example.paymentsystem.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "active_deals")
public class ActiveDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private AppUser merchant;

    @ManyToOne
    @JoinColumn(name = "partner_user_id", nullable = false)
    private AppUser partnerUser;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(name = "exchange_rate", nullable = false)
    private BigDecimal exchangeRate;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @ManyToOne
    @JoinColumn(name = "requisite_id")
    private TransferRequisite requisite;

    // Getters and Setters
    public TransferRequisite getRequisite() {
        return requisite;
    }

    public void setRequisite(TransferRequisite requisite) {
        this.requisite = requisite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getMerchant() {
        return merchant;
    }

    public void setMerchant(AppUser merchant) {
        this.merchant = merchant;
    }

    public AppUser getPartnerUser() {
        return partnerUser;
    }

    public void setPartnerUser(AppUser partnerUser) {
        this.partnerUser = partnerUser;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getMerchantId() {
        return merchant != null ? merchant.getId() : null;
    }

    public UUID getPartnerUserId() {
        return partnerUser != null ? partnerUser.getId() : null;
    }
}
