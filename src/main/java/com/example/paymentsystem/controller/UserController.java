package com.example.paymentsystem.controller;

import com.example.paymentsystem.model.AppUser; // Обновлено на AppUser
import com.example.paymentsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public AppUser registerUser(@RequestBody AppUser user) { // Обновлено на AppUser
        return userService.saveUser(user.getUsername(), user.getPassword());
    }

    @GetMapping("/{username}")
    public AppUser getUser(@PathVariable String username) { // Обновлено на AppUser
        return userService.findByUsername(username);
    }
}
