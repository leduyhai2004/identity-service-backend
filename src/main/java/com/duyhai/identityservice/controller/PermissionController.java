package com.duyhai.identityservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.duyhai.identityservice.dto.request.ApiResponse;
import com.duyhai.identityservice.dto.request.PermissionRequest;
import com.duyhai.identityservice.dto.response.PermissionResponse;
import com.duyhai.identityservice.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest req) {
        return ApiResponse.<PermissionResponse>builder()
                .result(this.permissionService.create(req))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(this.permissionService.findAll())
                .build();
    }

    @DeleteMapping("/{permissionName}")
    public ApiResponse<Void> deletePermission(@PathVariable String permissionName) {
        this.permissionService.delete(permissionName);
        return ApiResponse.<Void>builder().build();
    }
}
