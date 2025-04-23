package com.duyhai.identityservice.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.duyhai.identityservice.dto.request.RoleRequest;
import com.duyhai.identityservice.dto.response.RoleResponse;
import com.duyhai.identityservice.mapper.RoleMapper;
import com.duyhai.identityservice.repository.PermissionRepository;
import com.duyhai.identityservice.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest req) {
        var role = roleMapper.toRole(req);

        var permissions = permissionRepository.findAllById(req.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        var roles = roleRepository.findAll();
        return roles.stream().map(role -> roleMapper.toRoleResponse(role)).toList();
    }

    public void deleteRole(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
