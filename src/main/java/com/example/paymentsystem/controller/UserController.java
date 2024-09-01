package com.example.paymentsystem.controller;

import com.example.paymentsystem.model.ActiveDeal;
import com.example.paymentsystem.model.AppUser;
import com.example.paymentsystem.model.ConfirmationToken;
import com.example.paymentsystem.model.UserGroup;
import com.example.paymentsystem.service.ActiveDealService;
import com.example.paymentsystem.service.ConfirmationTokenService;
import com.example.paymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private ActiveDealService activeDealService;

    @PostMapping("/register")
    public AppUser registerUser(@RequestBody AppUser user) {
        return userService.registerUser(user.getEmail(), user.getUsername());
    }

    @GetMapping("/{username}")
    public AppUser getUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/existinguser/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable UUID id) {
        AppUser user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/existinguser")
    public ResponseEntity<AppUser> getUserByUsername(@RequestParam String username) {
        AppUser user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable UUID id, @RequestBody AppUser updatedUser) {
        AppUser existingUser = userService.findById(id);
        if (existingUser != null) {
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setTelegramToken(updatedUser.getTelegramToken());
            existingUser.setBalance(updatedUser.getBalance());
            existingUser.setSecretKey(updatedUser.getSecretKey());
            existingUser.setUserGroup(updatedUser.getUserGroup() == null ? UserGroup.REGULAR : updatedUser.getUserGroup());
            existingUser.setIsActive(updatedUser.getIsActive());
            existingUser.setWalletAddress(updatedUser.getWalletAddress());
            // обновим и другие поля, если это необходимо
            userService.save(existingUser);
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        if (userService.findById(id) != null) {
            userService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam("token") String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);

        if (confirmationToken == null || confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный или истекший токен");
        }

        AppUser user = confirmationToken.getUser();
        user.setIsActive(true);  // Активируем пользователя
        userService.save(user); // Сохраняем изменения

        return ResponseEntity.ok("Подтверждение прошло успешно!");
    }

    @PostMapping("/merchant-request")
    public ResponseEntity<AppUser> submitMerchantRequest(
            @RequestParam UUID userId,
            @RequestParam String walletAddress) {
        try {
            AppUser user = userService.submitMerchantRequest(userId, walletAddress);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/create-deal")
    public ResponseEntity<ActiveDeal> createDeal(
            @RequestParam UUID userId,
            @RequestParam UUID requisitesId,
            @RequestParam BigDecimal amount,
            @RequestParam BigDecimal course) {
        try {
            ActiveDeal deal = activeDealService.createDeal(userId, requisitesId, amount, course);
            return ResponseEntity.ok(deal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
