package com.example.paymentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.paymentsystem.model.TransferRequisite;

import java.util.UUID;

public interface TransferRequisiteRepository extends JpaRepository<TransferRequisite, UUID> {
    // Дополнительные методы
}
