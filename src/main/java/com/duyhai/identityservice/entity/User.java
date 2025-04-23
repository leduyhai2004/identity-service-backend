package com.duyhai.identityservice.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // cac thuoc tinh deu de private nen kh can set
public class User {
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

    @ManyToMany
    Set<Role> roles;
}
