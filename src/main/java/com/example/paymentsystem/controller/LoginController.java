package com.example.paymentsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/user-login") // Измененный маршрут
    public String getLoginPage() {
        return "login"; // Имя представления, которое будет возвращено
    }
}