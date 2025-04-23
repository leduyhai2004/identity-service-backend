package com.duyhai.identityservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.duyhai.identityservice.dto.request.ApiResponse;
import com.duyhai.identityservice.dto.request.RoleRequest;
import com.duyhai.identityservice.dto.response.RoleResponse;
import com.duyhai.identityservice.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest req) {
        return ApiResponse.<RoleResponse>builder()
                .result(this.roleService.createRole(req))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRole() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(this.roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{roleName}")
    public ApiResponse<Void> deleteRole(@PathVariable String roleName) {
        this.roleService.deleteRole(roleName);
        return ApiResponse.<Void>builder().build();
    }
}
