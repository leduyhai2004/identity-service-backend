package com.duyhai.identityservice.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.duyhai.identityservice.dto.request.UserCreationRequest;
import com.duyhai.identityservice.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc // Mock request
@Testcontainers // Phai co docker nhe, mySQL kh chay dc -))
public class UserControllerIntegrationTest {
    @Container
    static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void configDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc; // goi toi api cua chung ta
    // du lieu
    private UserCreationRequest userCreationRequest;
    private User user;

    @BeforeEach
    void initData() {
        userCreationRequest = UserCreationRequest.builder()
                .username("abcef")
                .password("123")
                .email("abcef@gmail.com")
                .phone("02034234")
                .address("234234")
                .gender("Male")
                .birthday(LocalDate.parse("2004-07-02"))
                .age(20)
                .build();

        user = User.builder()
                .id(6)
                .username("abcef")
                .email("abcef@gmail.com")
                .phone("02034234")
                .address("234234")
                .gender("Male")
                .birthday(LocalDate.parse("2004-07-02"))
                .age(20)
                .build();
    }

    @Test
    public void addUser_validRequest_success() throws Exception {
        // GIVEN : du doan dc ket qua xay ra ntn
        ObjectMapper objectMapper = new ObjectMapper();
        // dang ki cai nay moi chuyen cai LocalDate ve String dc
        objectMapper.registerModule(new JavaTimeModule());
        // chuyen ve String
        String content = objectMapper.writeValueAsString(userCreationRequest);

        // config sao cho giong voi trong service, day la mock day

        // WHEN : Khi chung ta test cai gi, vd : khi ma chung ta goi api, tao request
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)) // phai la String
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value(7));
        // THEN : expect dc ket qua la gi , xem o tren nh andExpect
    }
}
