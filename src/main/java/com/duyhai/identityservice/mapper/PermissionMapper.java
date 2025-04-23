package com.duyhai.identityservice.mapper;

import org.mapstruct.Mapper;

import com.duyhai.identityservice.dto.request.PermissionRequest;
import com.duyhai.identityservice.dto.response.PermissionResponse;
import com.duyhai.identityservice.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
