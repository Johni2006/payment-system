package com.example.paymentsystem.service;

import com.example.paymentsystem.model.AppUser;
import com.example.paymentsystem.model.ConfirmationToken;
import com.example.paymentsystem.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken getToken(String token) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);
        return confirmationToken.orElse(null); // Возвращаем объект или null, если токен не найден
    }

    public ConfirmationToken createTokenForUser(AppUser user) {
        ConfirmationToken token = new ConfirmationToken(user);
        confirmationTokenRepository.save(token);
        return token;
    }

    public Optional<ConfirmationToken> findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }
}
