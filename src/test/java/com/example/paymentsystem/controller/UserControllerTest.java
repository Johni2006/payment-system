package com.example.paymentsystem.controller;

import com.example.paymentsystem.model.AppUser;
import com.example.paymentsystem.model.ConfirmationToken;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    public void testConfirmUser() throws Exception {
        // Создаем пользователя и сохраняем его в базе данных
        AppUser testUser = new AppUser();
        testUser.setEmail("confirmuser@example.com");
        testUser.setUsername("confirmuser");
        testUser.setSecretKey(UUID.randomUUID().toString());
        testUser.setUserGroup(UserGroup.REGULAR);
        testUser.setIsActive(false);

        // Используем массив для хранения ссылки на testUser
        final AppUser[] savedUser = new AppUser[1];
        savedUser[0] = userRepository.save(testUser);

        // Создаем и сохраняем токен подтверждения
        ConfirmationToken confirmationToken = new ConfirmationToken(savedUser[0]);
        confirmationTokenRepository.save(confirmationToken);

        // Выполняем запрос на подтверждение пользователя
        mockMvc.perform(get("/api/users/confirm")
                        .param("token", confirmationToken.getToken()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    AppUser confirmedUser = userRepository.findById(savedUser[0].getId()).orElseThrow();
                    assert confirmedUser.getIsActive(); // Проверяем, что пользователь активирован
                });
    }

    @Test
    public void testRegisterUserWithExistingEmail() throws Exception {
        String existingUserJson = "{\"email\": \"existinguser@example.com\", \"username\": \"existinguser\"}";

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(existingUserJson))
                .andExpect(status().isBadRequest())  // Ожидаем статус ошибки
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result ->
                        assertEquals("Пользователь с таким email уже существует.", result.getResolvedException().getMessage()));
    }

    @Test
    public void testConfirmUserWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/users/confirm")
                        .param("token", "invalid-token"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateNonExistentUser() throws Exception {
        String updatedUserJson = "{\"email\": \"nonexistentuser@example.com\", \"username\": \"nonexistentuser\"}";

        mockMvc.perform(put("/api/users/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isNotFound());
    }


}
