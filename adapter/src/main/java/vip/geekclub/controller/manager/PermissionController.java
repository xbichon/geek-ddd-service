package vip.geekclub.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.common.controller.ApiResponse;
import vip.geekclub.config.security.UserSession;
import vip.geekclub.security.permission.query.PermissionQueryService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class PermissionController {

    private final PermissionQueryService PermissionQueryService;

    @GetMapping("/permission")
    public ApiResponse<Set<String>> getPermissionByUserId(UserSession userSession) {

        Set<String> permissions = PermissionQueryService.getPermissionByUserId(userSession.getUserId());
        return ApiResponse.success(permissions);
    }
}
