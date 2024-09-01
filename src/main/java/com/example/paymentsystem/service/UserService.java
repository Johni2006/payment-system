package com.example.paymentsystem.service;

import com.example.paymentsystem.model.AppUser;
import com.example.paymentsystem.model.ConfirmationToken;
import com.example.paymentsystem.model.UserGroup;
import com.example.paymentsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe


    public AppUser registerUser(String email, String username) {
        // Создаем нового пользователя
        AppUser user = new AppUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setUserGroup(UserGroup.REGULAR); // Установка значения группы по умолчанию
        user.setTelegramToken(generateTelegramToken());
        user.setSecretKey(generateSecretKey());

        // Сохраняем пользователя в базе данных
        AppUser savedUser = userRepository.save(user);

        // Создаем и сохраняем токен подтверждения
        ConfirmationToken confirmationToken = new ConfirmationToken(savedUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // Возвращаем сохраненного пользователя
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
        // Генерация токена подтверждения
        String confirmationToken = UUID.randomUUID().toString();

        // Логика отправки письма (здесь нужно использовать соответствующий сервис для отправки почты)
        // Например, используя JavaMailSender или стороннюю библиотеку.

        // Сохранение токена в базе данных для дальнейшего использования
        // Например, через создание сущности ConfirmationToken и соответствующего репозитория.
    }

    public boolean confirmUser(String token) {
        // Логика подтверждения пользователя по токену
        // Здесь вы должны проверить наличие токена в базе данных и подтвердить пользователя

        // Если токен найден и активен, то подтверждаем пользователя
        // Например:
        // AppUser user = findUserByToken(token);
        // user.setConfirmed(true);
        // userRepository.save(user);
        return true; // Если подтверждение успешно
    }
}
