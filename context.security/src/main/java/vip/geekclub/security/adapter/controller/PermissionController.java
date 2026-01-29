package vip.geekclub.security.adapter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.security.application.query.PermissionQueryService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class PermissionController {

    private final PermissionQueryService permissionQueryService;

    @GetMapping("/permission")
    public ApiResponse<Set<String>> getPermissionByUserId(UserPrincipal userPrincipal) {

        Set<String> permissions = permissionQueryService.getPermissionByAuthId(userPrincipal.authId());
        return ApiResponse.success(permissions);
    }
}
