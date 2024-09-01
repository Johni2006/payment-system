package com.example.paymentsystem.controller;

import com.example.paymentsystem.model.AppUser;
import com.example.paymentsystem.model.UserGroup;
import com.example.paymentsystem.repository.ConfirmationTokenRepository;
import com.example.paymentsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    private UUID testUserId;

    @BeforeEach
    public void setup() {
        // Удаляем все связанные токены перед удалением пользователей
        confirmationTokenRepository.deleteAll();

        // Убедимся, что база данных пуста перед каждым тестом
        userRepository.deleteAll();

        // Создадим тестового пользователя для использования в тестах
        AppUser testUser = new AppUser();
        testUser.setEmail("existinguser@example.com");
        testUser.setUsername("existinguser");
        testUser.setSecretKey(UUID.randomUUID().toString());
        testUser.setUserGroup(UserGroup.REGULAR);
        testUser.setIsActive(true);
        testUser = userRepository.save(testUser);
        testUserId = testUser.getId();
    }

    @Test
    public void testCreateUser() throws Exception {
        String userJson = "{\"email\": \"testuser@example.com\", \"username\": \"testuser\", \"password\": \"securepassword\"}";

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testGetUser() throws Exception {
        mockMvc.perform(get("/api/users/existinguser")
                        .param("username", "existinguser"))  // Передаем параметр 'username'
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("existinguser"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        String updatedUserJson = "{\"email\": \"updateduser@example.com\", \"username\": \"updateduser\"}";

        mockMvc.perform(put("/api/users/{id}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updateduser@example.com"))
                .andExpect(jsonPath("$.username").value("updateduser"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", testUserId))
                .andExpect(status().isNoContent()); // Изменено с isOk() на isNoContent()

        mockMvc.perform(get("/existinguser")
                        .param("username", "existinguser"))
                .andExpect(status().isNotFound());
    }
}
