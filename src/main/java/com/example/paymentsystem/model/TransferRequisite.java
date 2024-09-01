package com.example.paymentsystem.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "transfer_requisites", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "user_id"})
})
public class TransferRequisite {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "phone_or_card_number", length = 20)
    private String phoneOrCardNumber;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getPhoneOrCardNumber() {
        return phoneOrCardNumber;
    }

    public void setPhoneOrCardNumber(String phoneOrCardNumber) {
        this.phoneOrCardNumber = phoneOrCardNumber;
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
}
