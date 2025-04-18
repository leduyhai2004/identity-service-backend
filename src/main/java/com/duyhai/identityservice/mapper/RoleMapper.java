package com.duyhai.identityservice.mapper;

import com.duyhai.identityservice.dto.request.PermissionRequest;
import com.duyhai.identityservice.dto.request.RoleRequest;
import com.duyhai.identityservice.dto.response.PermissionResponse;
import com.duyhai.identityservice.dto.response.RoleResponse;
import com.duyhai.identityservice.entity.Permission;
import com.duyhai.identityservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions" , ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
