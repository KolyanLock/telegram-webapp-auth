package org.nikolait.assigment.telegramwebappauth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.nikolait.assigment.telegramwebappauth.config.TestcontainersConfiguration;
import org.nikolait.assigment.telegramwebappauth.dto.TelegramUser;
import org.nikolait.assigment.telegramwebappauth.entity.User;
import org.nikolait.assigment.telegramwebappauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import(TestcontainersConfiguration.class)
public abstract class BaseIntegrationTest {

    protected static final List<TelegramUser> TEST_USERS = List.of(
            new TelegramUser(111L, "John", "Doe", "johndoe", "en", true, "http://photo1.url"),
            new TelegramUser(222L, "Jane", "Smith", "janesmith", "en", true, "http://photo2.url"),
            new TelegramUser(333L, "Bob", "Johnson", "bobjohnson", "en", true, "http://photo3.url")
    );

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    void setUp() {
        List<User> users = TEST_USERS.stream()
                .map(tg -> modelMapper.map(tg, User.class))
                .toList();
        userRepository.saveAll(users);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
} 