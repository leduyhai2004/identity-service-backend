package com.duyhai.identityservice.mapper;

import com.duyhai.identityservice.dto.request.UserCreationRequest;
import com.duyhai.identityservice.dto.request.UserUpdateRequest;
import com.duyhai.identityservice.dto.response.UserResponse;
import com.duyhai.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
