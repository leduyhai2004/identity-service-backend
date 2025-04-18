package com.duyhai.identityservice.mapper;

import com.duyhai.identityservice.dto.request.PermissionRequest;
import com.duyhai.identityservice.dto.response.PermissionResponse;
import com.duyhai.identityservice.entity.Permission;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
