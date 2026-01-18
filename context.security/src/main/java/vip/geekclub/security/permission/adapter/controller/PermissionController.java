package vip.geekclub.security.permission.adapter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.common.controller.ApiResponse;
import vip.geekclub.common.security.UserSession;
import vip.geekclub.security.permission.application.query.PermissionQueryService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class PermissionController {

    private final PermissionQueryService permissionQueryService;

    @GetMapping("/permission")
    public ApiResponse<Set<String>> getPermissionByUserId(UserSession userSession) {

        Set<String> permissions = permissionQueryService.getPermissionByUserId(userSession.userId());
        return ApiResponse.success(permissions);
    }
}
