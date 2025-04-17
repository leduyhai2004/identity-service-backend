package com.duyhai.identityservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // cac thuoc tinh deu de private nen kh can set
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String username;
    String password;
    String email;
    String phone;
    String address;
    String gender;
    LocalDate birthday;
    int age;

    @ElementCollection
    Set<String> roles;
}
