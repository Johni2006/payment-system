package com.example.paymentsystem.service;

import com.example.paymentsystem.model.AppUser;
import com.example.paymentsystem.model.ConfirmationToken;
import com.example.paymentsystem.model.UserGroup;
import com.example.paymentsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

    private static final SecureRandom secureRandom = new SecureRandom(); // threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); // threadsafe

    @Autowired
    public UserService(UserRepository userRepository,
                       EmailService emailService,
                       ConfirmationTokenService confirmationTokenService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
    }


    public AppUser registerUser(String email, String username) {
        logger.info("Регистрация нового пользователя с email: {}", email);
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует.");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует.");
        }

        AppUser user = new AppUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setUserGroup(UserGroup.REGULAR);
        user.setTelegramToken(generateTelegramToken());
        user.setSecretKey(generateSecretKey());

        AppUser savedUser = userRepository.save(user);

        ConfirmationToken confirmationToken = confirmationTokenService.createTokenForUser(savedUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        sendConfirmationEmail(savedUser);

        return savedUser;
    }

    public String generateTelegramToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public String generateSecretKey() {
        return UUID.randomUUID().toString();
    }

    public AppUser findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public AppUser findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public void save(AppUser user) {
        userRepository.save(user);
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public void sendConfirmationEmail(AppUser user) {
        logger.info("Отправка подтверждающего письма на email: {}", user.getEmail());

        ConfirmationToken confirmationToken = confirmationTokenService.createTokenForUser(user);

        String confirmationUrl = "http://localhost:8080/api/users/confirm?token=" + confirmationToken.getToken();

        String subject = "Подтверждение регистрации";
        String message = "Для подтверждения регистрации перейдите по следующей ссылке: " + confirmationUrl;
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    public boolean confirmUser(String token) {
        logger.info("Подтверждение пользователя с токеном: {}", token);
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Токен не найден или истек."));

        AppUser user = confirmationToken.getUser();
        user.setIsActive(true);
        userRepository.save(user);

        return true;
    }
}

