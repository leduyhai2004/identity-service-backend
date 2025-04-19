package com.duyhai.identityservice.dto.request;

import com.duyhai.identityservice.validator.DobConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 3, message = "PASSWORD_INVALID")
    String password;

    @Email(message = "Invalid Email")
    String email;
    String phone;
    String address;
    String gender;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    LocalDate birthday;
    int age;
}
