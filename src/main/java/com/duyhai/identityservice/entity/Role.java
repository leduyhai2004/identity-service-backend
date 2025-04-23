package com.duyhai.identityservice.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) //
public class Role {
    @Id
    String name;

    String description;

    @ManyToMany
    Set<Permission> permissions;
}
