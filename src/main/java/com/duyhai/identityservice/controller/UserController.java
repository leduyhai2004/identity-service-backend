package com.duyhai.identityservice.controller;

import com.duyhai.identityservice.dto.request.ApiResponse;
import com.duyhai.identityservice.dto.request.UserCreationRequest;
import com.duyhai.identityservice.dto.request.UserUpdateRequest;
import com.duyhai.identityservice.dto.response.UserResponse;
import com.duyhai.identityservice.entity.User;
import com.duyhai.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping("/users")
    public ApiResponse<User> addUser(@RequestBody @Valid UserCreationRequest userReq) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(userReq));
        return apiResponse;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("username" + authentication.getName());
        authentication.getAuthorities().forEach(authority -> log.info("authority" + authority.getAuthority()));
        return this.userService.findAll();
    }

    @GetMapping("/user/{id}")
    public UserResponse getDetailUser(@PathVariable("id") long id) {
        return this.userService.getUser(id);
    }

    @PutMapping("/user/{id}")
    public UserResponse updateUser(@RequestBody UserUpdateRequest userReq, @PathVariable("id") long id) {
        System.out.println(userReq.toString());
        return this.userService.updateUser(id,userReq);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        this.userService.deleteUser(id);
    }

    @GetMapping("/user/myInfo")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(this.userService.getMyInfo()).build();
    }
}
