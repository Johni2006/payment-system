package com.example.paymentsystem.repository;

import com.example.paymentsystem.model.ActiveDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActiveDealRepository extends JpaRepository<ActiveDeal, UUID> {
    // Добавьте методы для поиска по необходимости
}
