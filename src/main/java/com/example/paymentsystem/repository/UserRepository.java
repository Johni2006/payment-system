package com.example.paymentsystem.repository;

import com.example.paymentsystem.model.AppUser; // Обновлено на AppUser
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> { // Обновлено на AppUser
    AppUser findByUsername(String username); // Обновлено на AppUser
}
