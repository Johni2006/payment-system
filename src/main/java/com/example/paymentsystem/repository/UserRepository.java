package com.example.paymentsystem.repository;

import com.example.paymentsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // Здесь можно добавить кастомные методы для поиска пользователей, если потребуется
}
