package com.akulov.springboot.userservice_withkafka.integration;

import com.akulov.springboot.userservice_withkafka.dto.UserDto;
import com.akulov.springboot.userservice_withkafka.enums.OperationType;
import com.akulov.springboot.userservice_withkafka.repository.UserRepository;
import com.akulov.springboot.userservice_withkafka.service.KafkaProducerService;
import com.akulov.springboot.userservice_withkafka.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
public class UserControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
    }

    @AfterAll
    static void tearDown() {
        postgresContainer.stop();
    }

    @DisplayName("POST /api/users — создаёт нового пользователя и отправляет Kafka уведомление")
    @Test
    void testAddUserSendsKafkaNotification() throws Exception {
        UserDto requestDto = new UserDto();
        requestDto.setName("Sarah");
        requestDto.setEmail("sarah@example.com");
        requestDto.setAge(29);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sarah"))
                .andExpect(jsonPath("$.email").value("sarah@example.com"));

        Mockito.verify(kafkaProducerService, Mockito.times(1))
                .sendNotification(Mockito.eq(OperationType.CREATE),
                        Mockito.eq("sarah@example.com"),
                        Mockito.eq("Sarah"));
    }

    @DisplayName("DELETE /api/users/{id} — удаляет пользователя и отправляет Kafka уведомление")
    @Test
    void testDeleteUserSendsKafkaNotification() throws Exception {
        userDto = new UserDto();
        userDto.setName("John");
        userDto.setEmail("john@example.com");
        userDto.setAge(10);
        userDto = userService.addUser(userDto);

        mockMvc.perform(delete("/api/users/" + userDto.getId()))
                .andExpect(status().isNoContent());

        Mockito.verify(kafkaProducerService, Mockito.times(1))
                .sendNotification(Mockito.eq(OperationType.DELETE),
                        Mockito.eq("john@example.com"),
                        Mockito.eq("John"));
    }
}
