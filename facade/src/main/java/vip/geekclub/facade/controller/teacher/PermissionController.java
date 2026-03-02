package vip.geekclub.facade.controller.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.facade.service.SecurityFacade;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher/permission")
public class PermissionController {

    private final SecurityFacade securityFacade;

    @GetMapping("/current")
    public ApiResponse<Set<String>> getPermissionByUserId(UserPrincipal userPrincipal) {

        Set<String> permissions = securityFacade.getPermissionsByAuthId(userPrincipal.authId());
        return ApiResponse.success(permissions);
    }
}
