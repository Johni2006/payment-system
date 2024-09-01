package com.example.paymentsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.paymentsystem.model.ActiveDeal;
import com.example.paymentsystem.model.AppUser;
import com.example.paymentsystem.model.UserGroup;
import com.example.paymentsystem.repository.ActiveDealRepository;
import com.example.paymentsystem.repository.UserRepository;
import com.example.paymentsystem.service.ActiveDealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

class ActiveDealServiceTest {

    @Mock
    private ActiveDealRepository activeDealRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ActiveDealService activeDealService;

    private AppUser merchant;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        merchant = new AppUser();
        merchant.setId(UUID.randomUUID());
        merchant.setUserGroup(UserGroup.REGULAR);
        merchant.setIsActive(true);
        merchant.setBalance(new BigDecimal("1000"));
    }

    @Test
    void testCreateDeal_Success() {
        when(userRepository.findAllByUserGroupAndIsActive(UserGroup.REGULAR, true))
                .thenReturn(Collections.singletonList(merchant));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new AppUser()));
        when(activeDealRepository.save(any(ActiveDeal.class))).thenReturn(new ActiveDeal());

        BigDecimal amount = new BigDecimal("100");
        BigDecimal course = new BigDecimal("10");

        ActiveDeal activeDeal = activeDealService.createDeal(userId, UUID.randomUUID(), amount, course);

        assertNotNull(activeDeal);
        verify(activeDealRepository, times(1)).save(any(ActiveDeal.class));
    }

    @Test
    void testCreateDeal_NoSuitableMerchant() {
        when(userRepository.findAllByUserGroupAndIsActive(UserGroup.REGULAR, true))
                .thenReturn(Collections.emptyList());

        BigDecimal amount = new BigDecimal("100");
        BigDecimal course = new BigDecimal("10");

        assertThrows(IllegalArgumentException.class, () -> {
            activeDealService.createDeal(userId, UUID.randomUUID(), amount, course);
        });
    }

    @Test
    void testCreateDeal_InsufficientMerchantBalance() {
        merchant.setBalance(new BigDecimal("5")); // Недостаточный баланс
        when(userRepository.findAllByUserGroupAndIsActive(UserGroup.REGULAR, true))
                .thenReturn(Collections.singletonList(merchant));

        BigDecimal amount = new BigDecimal("100");
        BigDecimal course = new BigDecimal("10");

        assertThrows(IllegalArgumentException.class, () -> {
            activeDealService.createDeal(userId, UUID.randomUUID(), amount, course);
        });
    }

}

