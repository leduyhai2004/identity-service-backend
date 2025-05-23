package com.duyhai.identityservice.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // cac thuoc tinh deu de private nen kh can set
public class InvalidToken {
    @Id
    String id;

    String token;
    Date expiredTime;
}
