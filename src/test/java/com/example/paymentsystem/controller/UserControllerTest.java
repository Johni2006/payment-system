package com.example.paymentsystem.controller;

import com.example.paymentsystem.dto.DealRequest;
import com.example.paymentsystem.model.*;
import com.example.paymentsystem.repository.ActiveDealRepository;
import com.example.paymentsystem.repository.ConfirmationTokenRepository;
import com.example.paymentsystem.repository.TransferRequisiteRepository;
import com.example.paymentsystem.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

    @Autowired
    private ActiveDealRepository activeDealRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransferRequisiteRepository requisiteRepository;

    private String testUserId;
    private String testRequisiteId;

    @BeforeEach
    public void setup() {
        // Удаляем все связанные токены перед удалением пользователей
        confirmationTokenRepository.deleteAll();

        // Убедимся, что база данных пуста перед каждым тестом
        userRepository.deleteAll();
        activeDealRepository.deleteAll();

        // Создайте тестового пользователя с указанным ID
        AppUser testUser = new AppUser();
        testUser.setId(UUID.randomUUID()); // Используйте тот же ID UUID.fromString("5d8fc345-d9ab-4062-8f8f-2a783208df72")
        testUser.setEmail("existinguser@example.com");
        testUser.setUsername("existinguser");
        testUser.setSecretKey(UUID.randomUUID().toString());
        testUser.setUserGroup(UserGroup.REGULAR);
        testUser.setIsActive(true);
        testUser.setBalance(new BigDecimal("5000.00"));
        testUserId = testUser.getId().toString();
        userRepository.save(testUser);

        // Также создайте тестового мерчанта, если это необходимо
        AppUser testMerchant = new AppUser();
        testMerchant.setId(UUID.randomUUID());
        testMerchant.setEmail("merchant@example.com");
        testMerchant.setUsername("merchant");
        testMerchant.setSecretKey(UUID.randomUUID().toString());
        testMerchant.setUserGroup(UserGroup.REGULAR);
        testMerchant.setIsActive(true);
        testMerchant.setBalance(new BigDecimal("10000.00"));

        userRepository.save(testMerchant);

        // Создаем новый объект TransferRequisite
        TransferRequisite requisite = new TransferRequisite();
        requisite.setId(UUID.randomUUID());
        requisite.setName("Test Requisite");
        requisite.setPhoneOrCardNumber("1234567890");
        requisite.setComment("Test Comment");
        requisite.setOrderId(UUID.randomUUID());
        requisite.setUser(testUser);

        testRequisiteId = requisite.getId().toString();

        // Сохраняем объект в базе данных, используя метод save()
        requisite = requisiteRepository.save(requisite);

        // Проверяем, что ID был автоматически присвоен
        assertNotNull(requisite.getId());
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

    @Test
    public void testCreateDeal() throws Exception {
        String dealRequestJson = "{\"userId\":\"" + testUserId + "\",\"requisitesId\":\"" + testRequisiteId + "\",\"amount\":500,\"course\":10}";

        mockMvc.perform(post("/api/users/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dealRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.merchantId").exists())
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.currency").value("USDT"));
    }


}
