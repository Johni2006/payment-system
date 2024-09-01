package com.example.paymentsystem.repository;

import com.example.paymentsystem.model.AppUser; // Обновлено на AppUser
import com.example.paymentsystem.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByUsername(String username);
    // Поиск всех пользователей определенной группы и активности
    List<AppUser> findAllByUserGroupAndIsActive(UserGroup userGroup, boolean isActive);

}
