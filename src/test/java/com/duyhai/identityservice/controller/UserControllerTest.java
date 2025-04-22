package com.duyhai.identityservice.controller;

import com.duyhai.identityservice.dto.request.UserCreationRequest;
import com.duyhai.identityservice.dto.response.UserResponse;
import com.duyhai.identityservice.entity.User;
import com.duyhai.identityservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc // Mock request
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // goi toi api cua chung ta

    @MockBean
    private UserService userService;

    //du lieu
    private UserCreationRequest userCreationRequest;
    private User user;

    @BeforeEach
     void initData(){
        userCreationRequest = UserCreationRequest.builder()
                .username("abc")
                .password("123")
                .email("abc@gmail.com")
                .phone("02034234")
                .address("234234")
                .gender("Male")
                .birthday(LocalDate.parse("2004-07-02"))
                .age(20)
                .build();

        user = User.builder()
                .id(6)
                .username("abc")
                .email("abc@gmail.com")
                .phone("02034234")
                .address("234234")
                .gender("Male")
                .birthday(LocalDate.parse("2004-07-02"))
                .age(20)
                .build();
    }

    @Test
    public void addUser_validRequest_success() throws Exception {
        //GIVEN : du doan dc ket qua xay ra ntn
        ObjectMapper objectMapper = new ObjectMapper();
        // dang ki cai nay moi chuyen cai LocalDate ve String dc
        objectMapper.registerModule(new JavaTimeModule());
        //chuyen ve String
        String content = objectMapper.writeValueAsString(userCreationRequest);

        //config sao cho giong voi trong service, day la mock day
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(user);

        //WHEN : Khi chung ta test cai gi, vd : khi ma chung ta goi api, tao request
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)) // phai la String
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value(6));
        //THEN : expect dc ket qua la gi , xem o tren nh andExpect
    }

    @Test
    public void addUser_invalidRequest_fail() throws Exception {
        //GIVEN : du doan dc ket qua xay ra ntn\
        userCreationRequest.setUsername("ha");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        //config
//        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(user);

        //WHEN : Khi chung ta test cai gi, vd : khi ma chung ta goi api, tao request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)) // phai la String
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 3 characters"));
        //THEN : expect dc ket qua la gi , xem o tren nh andExpect
    }
}
