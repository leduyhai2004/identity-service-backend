package com.duyhai.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.duyhai.identityservice.dto.request.RoleRequest;
import com.duyhai.identityservice.dto.response.RoleResponse;
import com.duyhai.identityservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
