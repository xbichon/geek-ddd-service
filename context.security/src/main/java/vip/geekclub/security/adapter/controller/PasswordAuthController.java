package vip.geekclub.security.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.security.SessionStore;
import vip.geekclub.framework.security.UserAuthentication;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.security.adapter.controller.dto.CaptchaResponse;
import vip.geekclub.security.adapter.controller.dto.PasswordLoginRequest;
import vip.geekclub.security.application.command.credential.PasswordVerificationCommand;
import vip.geekclub.security.application.query.PrincipalQueryService;
import vip.geekclub.support.CaptchaKit;

/**
 * 用户名密码认证控制器
 * 处理传统的用户名密码登录功能
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/security/auth")
public class PasswordAuthController {

    private final SessionStore authSessionManager;
    private final PrincipalQueryService principalQueryService;
    private final CaptchaKit captchaKit;

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;
    private final CommandBus commandBus;

    /**
     * 用户名密码登录
     *
     * @param request 登录请求
     * @return JWT Token
     */
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid PasswordLoginRequest request) {

        // 非开发环境才验证验证码
        if (!"dev".equals(activeProfile)) {
            var result = captchaKit.validate(request.captchaKey(), request.captcha());
            if (result.isFail()) {
                return ApiResponse.fail(400, result.errorMessage());
            }
        }

        // 验证码验证通过，继续执行登录逻辑
        // 1. 命令端验证密码，获取 authId
        PasswordVerificationCommand command = new PasswordVerificationCommand(request.userType(), request.identifier(), request.password());
        String authId = commandBus.dispatch(command);

        // 2. 查询端获取用户信息（CQRS：命令与查询分离）
        String userType = principalQueryService.getUserTypeByAuthId(authId);
        UserPrincipal userPrincipal = new UserPrincipal(authId, userType);
        UserAuthentication userAuthentication = new UserAuthentication(userPrincipal);
        String jwtToken = authSessionManager.create(userAuthentication);

        return ApiResponse.success(jwtToken);
    }

    /**
     * 生成验证码
     * 生成验证码图片和UUID key并一起返回给前端
     *
     * @return 验证码结果，包含图片Base64编码和UUID key
     */
    @GetMapping("/captcha")
    public ApiResponse<CaptchaResponse> captcha() {
        CaptchaKit.CaptchaResult result = captchaKit.generate();
        CaptchaResponse response = new CaptchaResponse(result.captchaKey(), result.imageData());
        return ApiResponse.success(response);
    }
}
