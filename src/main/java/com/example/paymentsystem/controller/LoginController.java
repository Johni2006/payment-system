package com.example.paymentsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model) {
        // Добавьте любые необходимые параметры в модель, если нужно
        return "login"; // Вернет страницу login.html
    }
}
