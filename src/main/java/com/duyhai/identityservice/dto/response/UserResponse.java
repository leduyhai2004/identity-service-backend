package com.duyhai.identityservice.dto.response;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.ElementCollection;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String username;
    String email;
    String phone;
    String address;
    String gender;
    LocalDate birthday;
    int age;

    @ElementCollection
    Set<RoleResponse> roles;
}
