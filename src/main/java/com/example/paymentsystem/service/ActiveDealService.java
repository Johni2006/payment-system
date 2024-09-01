package com.example.paymentsystem.service;

import com.example.paymentsystem.model.ActiveDeal;
import com.example.paymentsystem.model.AppUser;
import com.example.paymentsystem.model.UserGroup;
import com.example.paymentsystem.repository.ActiveDealRepository;
import com.example.paymentsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ActiveDealService {

    @Autowired
    private ActiveDealRepository activeDealRepository;

    @Autowired
    private UserRepository userRepository;

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

        // Создать запись в active_deals
        ActiveDeal deal = new ActiveDeal();
        deal.setMerchant(selectedMerchant);
        deal.setPartnerUser(userRepository.findById(userId).orElseThrow());
        deal.setAmount(amount);
        deal.setCurrency("USDT");
        deal.setExchangeRate(course);
        deal.setOrderId(UUID.randomUUID());

        return activeDealRepository.save(deal);
    }
}
