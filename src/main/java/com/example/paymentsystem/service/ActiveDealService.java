package com.example.paymentsystem.service;

import com.example.paymentsystem.model.*;
import com.example.paymentsystem.repository.ActiveDealRepository;
import com.example.paymentsystem.repository.TransferRequisiteRepository;
import com.example.paymentsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ActiveDealService {

    @Autowired
    private ActiveDealRepository activeDealRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransferRequisiteRepository requisiteRepository;


    public ActiveDeal createDeal(UUID userId, UUID requisitesId, BigDecimal amount, BigDecimal course) {
        // Найти подходящего мерчанта
        List<AppUser> merchants = userRepository.findAllByUserGroupAndIsActive(UserGroup.REGULAR, true);
        AppUser selectedMerchant = null;

        for (AppUser merchant : merchants) {
            if (merchant.getBalance().compareTo(amount.multiply(course).divide(BigDecimal.TEN)) >= 0) {
                selectedMerchant = merchant;
                break;
            }
        }

        if (selectedMerchant == null) {
            throw new IllegalArgumentException("No suitable merchant found");
        }

        // Получить реквизиты
        TransferRequisite requisite = requisiteRepository.findById(requisitesId)
                .orElseThrow(() -> new NoSuchElementException("No requisites found with ID: " + requisitesId));

        // Создать запись в active_deals
        ActiveDeal deal = new ActiveDeal();
        deal.setMerchant(selectedMerchant);
        deal.setPartnerUser(userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("No user found with ID: " + userId)));
        deal.setAmount(amount);
        deal.setCurrency("USDT");
        deal.setExchangeRate(course);
        deal.setOrderId(UUID.randomUUID());
        deal.setRequisite(requisite); // Установить реквизиты

        return activeDealRepository.save(deal);
    }
}
