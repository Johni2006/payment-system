package com.example.paymentsystem.service;

import com.example.paymentsystem.model.AppUser; // Обновлено на AppUser
import com.example.paymentsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AppUser saveUser(String username, String password) { // Обновлено на AppUser
        String encodedPassword = passwordEncoder.encode(password);
        AppUser user = new AppUser(username, encodedPassword); // Обновлено на AppUser
        return userRepository.save(user);
    }

    public AppUser findByUsername(String username) { // Обновлено на AppUser
        return userRepository.findByUsername(username);
    }
}
