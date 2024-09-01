package com.example.paymentsystem.repository;

import com.example.paymentsystem.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {
    Optional<ConfirmationToken> findByToken(String token);
}
