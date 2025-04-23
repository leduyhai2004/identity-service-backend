package com.duyhai.identityservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duyhai.identityservice.dto.request.PermissionRequest;
import com.duyhai.identityservice.dto.response.PermissionResponse;
import com.duyhai.identityservice.entity.Permission;
import com.duyhai.identityservice.mapper.PermissionMapper;
import com.duyhai.identityservice.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest req) {
        Permission permission = permissionMapper.toPermission(req);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> findAll() {
        List<Permission> permissions = permissionRepository.findAll();
        List<PermissionResponse> permissionResponseList = permissions.stream()
                .map(permission -> permissionMapper.toPermissionResponse(permission))
                .toList();
        return permissionResponseList;
    }

    public void delete(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }
}
