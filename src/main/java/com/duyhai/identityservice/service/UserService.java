package com.duyhai.identityservice.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.duyhai.identityservice.constant.PredefinedRole;
import com.duyhai.identityservice.dto.request.UserCreationRequest;
import com.duyhai.identityservice.dto.request.UserUpdateRequest;
import com.duyhai.identityservice.dto.response.UserResponse;
import com.duyhai.identityservice.entity.Role;
import com.duyhai.identityservice.entity.User;
import com.duyhai.identityservice.exception.AppException;
import com.duyhai.identityservice.exception.ErrorCode;
import com.duyhai.identityservice.mapper.UserMapper;
import com.duyhai.identityservice.repository.RoleRepository;
import com.duyhai.identityservice.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    // @PreAuthorize("hasRole('ADMIN')")
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PreAuthorize("hasAnyAuthority('CREATE_DATA')")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User createUser(UserCreationRequest request) {

        if (this.userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(role -> roles.add(role));

        user.setRoles(roles);
        // user.setRoles(roles);
        return userRepository.save(user);
    }
    // nguoi dung dang dang nhap chi lay dc thong tin cua chinh minh ma thoi
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(long id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found user")));
    }

    public UserResponse updateUser(long id, UserUpdateRequest req) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found user"));
        userMapper.updateUser(user, req);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        var roles = roleRepository.findAllById(req.getRoles());
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
}
