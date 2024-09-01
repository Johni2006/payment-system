package com.example.paymentsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser // Этот пользователь будет использоваться для аутентификации в тесте
    public void testUserLoginRoute() throws Exception {
        mockMvc.perform(get("/user-login") // Тестируем измененный маршрут
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Добавляем CSRF токен к запросу
                .andExpect(status().isOk()) // Ожидаем статус 200
                .andExpect(view().name("login")); // Ожидаем, что будет возвращено представление "login"
    }

    @Test
    public void testUserLoginRouteWithCredentials() throws Exception {
        mockMvc.perform(post("/user-login") // Используем POST-запрос
                        .with(httpBasic("username", "password")) // Учетные данные
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Добавляем CSRF токен к запросу
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}
