package com.duyhai.identityservice.service;

import com.duyhai.identityservice.dto.request.UserCreationRequest;
import com.duyhai.identityservice.entity.User;
import com.duyhai.identityservice.exception.AppException;
import com.duyhai.identityservice.repository.UserRepository;

import org.assertj.core.api.Assertions;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest userCreationRequest;
    private User user;
    @Autowired
    private AssertTrueValidator assertTrueValidator;

    //chuan bi data
    @BeforeEach
    void initData(){
        userCreationRequest = UserCreationRequest.builder()
                .username("abcd")
                .password("123")
                .email("abc@gmail.com")
                .phone("02034234")
                .address("234234")
                .gender("Male")
                .birthday(LocalDate.parse("2004-07-02"))
                .age(20)
                .build();

        user = User.builder()
                .id(7)
                .username("abcd")
                .email("abc@gmail.com")
                .phone("02034234")
                .address("234234")
                .gender("Male")
                .birthday(LocalDate.parse("2004-07-02"))
                .age(20)
                .build();
    }
    @Test
    void createUser_validRequest_success(){
        //GIVEN
        //mock du lieu de hoat dong giong ta viet
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);

        //WHEN
        var response = userService.createUser(userCreationRequest);

        //THEN
        Assertions.assertThat(response.getId()).isEqualTo(7);
        Assertions.assertThat(response.getUsername()).isEqualTo("abcd");
    }
    @Test
    void createUser_userExisted_fail(){
        //GIVEN
        //mock: gia su sai du lieu o day va nem ra exception : USER_EXISTED
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);

        //WHEN
        //assertThrows voi 2 tham so, 1 la expected type of class, 2 la method
        var exception = assertThrows(AppException.class,
                () -> userService.createUser(userCreationRequest));
        // THEN: USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }
}
