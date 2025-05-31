package org.nikolait.assigment.telegramwebappauth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nikolait.assigment.telegramwebappauth.BaseIntegrationTest;
import org.nikolait.assigment.telegramwebappauth.dto.TelegramUser;
import org.nikolait.assigment.telegramwebappauth.entity.User;
import org.nikolait.assigment.telegramwebappauth.helper.TelegramInitDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIntegrationTest extends BaseIntegrationTest {

    private static final Long USER_ID = 555L;
    private static final String FIRST_NAME = "Rika";
    private static final String LAST_NAME = "Furude";
    private static final String USERNAME = "RikaFOyashiro";
    private static final String LANGUAGE_CODE = "en";
    private static final String PHOTO_URL = "http://photo5.url";

    @Autowired
    private TelegramInitDataGenerator initDataGenerator;

    private static final TelegramUser TEST_TELEGRAM_USER = new TelegramUser(
            USER_ID,
            FIRST_NAME,
            LAST_NAME,
            USERNAME,
            LANGUAGE_CODE,
            true,
            PHOTO_URL
    );

    private static final User TEST_USER = User.builder()
            .id(USER_ID)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .username(USERNAME)
            .languageCode(LANGUAGE_CODE)
            .allowsWriteToPm(true)
            .photoUrl(PHOTO_URL)
            .build();

    @Test
    @DisplayName("POST /api/user/register - success")
    void registerIfAbsent_success() throws Exception {
        String initData = initDataGenerator.generateValidInitData(TEST_TELEGRAM_USER);
        mockMvc.perform(post("/api/user/register")
                        .header("Authorization", "Telegram " + initData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User savedUser = userRepository.findById(TEST_TELEGRAM_USER.getId()).orElseThrow();
        assertEquals(USERNAME, savedUser.getUsername());
    }

    @Test
    @DisplayName("GET /api/user/info - success")
    void getInfo_success() throws Exception {
        userRepository.save(TEST_USER);
        String initData = initDataGenerator.generateValidInitData(TEST_TELEGRAM_USER);

        mockMvc.perform(get("/api/user/info")
                        .header("Authorization", "Telegram " + initData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.languageCode").value(LANGUAGE_CODE))
                .andExpect(jsonPath("$.allowsWriteToPm").value(true))
                .andExpect(jsonPath("$.photoUrl").value(PHOTO_URL));
    }

    @Test
    @DisplayName("GET /api/user/info for a non-existent user - not found")
    void getInfo_notFound() throws Exception {
        String initData = initDataGenerator.generateValidInitData(TEST_TELEGRAM_USER);

        mockMvc.perform(get("/api/user/info")
                        .header("Authorization", "Telegram " + initData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(org.hamcrest.Matchers.containsString("User not found")));
    }

    @Test
    @DisplayName("POST /api/user/register with fake initData - unauthorized")
    void registerIfAbsent_with_fake_initData_unauthorized() throws Exception {
        String fakeInitData = initDataGenerator.generateFakeInitData(TEST_TELEGRAM_USER);

        mockMvc.perform(post("/api/user/register")
                        .header("Authorization", "Telegram " + fakeInitData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/user/info with fake initData - unauthorized")
    void getInfo_with_fake_initData_unauthorized() throws Exception {
        userRepository.save(TEST_USER);
        String fakeInitData = initDataGenerator.generateFakeInitData(TEST_TELEGRAM_USER);
        mockMvc.perform(get("/api/user/info")
                        .header("Authorization", "Telegram " + fakeInitData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/user/register with expired initData - unauthorized")
    void registerIfAbsent_with_expired_initData_unauthorized() throws Exception {
        String fakeInitData = initDataGenerator.generateExpiredInitData(TEST_TELEGRAM_USER);

        mockMvc.perform(post("/api/user/register")
                        .header("Authorization", "Telegram " + fakeInitData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/user/info with expired initData - unauthorized")
    void getInfo_with_expired_initData_unauthorized() throws Exception {
        userRepository.save(TEST_USER);
        String fakeInitData = initDataGenerator.generateExpiredInitData(TEST_TELEGRAM_USER);
        mockMvc.perform(get("/api/user/info")
                        .header("Authorization", "Telegram " + fakeInitData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
} 