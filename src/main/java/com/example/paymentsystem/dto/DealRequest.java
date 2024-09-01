package com.example.paymentsystem.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class DealRequest {

    private UUID userId;
    private UUID requisitesId;
    private BigDecimal amount;
    private BigDecimal course;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRequisitesId() {
        return requisitesId;
    }

    public void setRequisitesId(UUID requisitesId) {
        this.requisitesId = requisitesId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCourse() {
        return course;
    }

    public void setCourse(BigDecimal course) {
        this.course = course;
    }
}
