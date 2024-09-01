package com.example.paymentsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Вернуть страницу логина, если требуется
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Вернуть основную страницу пользователя после успешного входа
    }

    @GetMapping("/oauth2/redirect")
    public String redirectOAuth() {
        return "redirect:/dashboard";
    }
}
