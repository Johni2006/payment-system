package com.example.paymentsystem.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class DealResponseDto {
    private Long id;
    private UUID merchantId;
    private UUID partnerUserId;
    private BigDecimal amount;
    private String currency;
    private BigDecimal exchangeRate;
    private UUID orderId;

    public DealResponseDto(Long id, UUID merchantId, UUID partnerUserId, BigDecimal amount, String currency, BigDecimal exchangeRate, UUID orderId) {
        this.id = id;
        this.merchantId = merchantId;
        this.partnerUserId = partnerUserId;
        this.amount = amount;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.orderId = orderId;
    }

    // Геттеры

    public Long getId() {
        return id;
    }

    public UUID getMerchantId() {
        return merchantId;
    }

    public UUID getPartnerUserId() {
        return partnerUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
