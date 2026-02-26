package vip.geekclub.security.adapter.controller.student;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import vip.geekclub.contract.UserType;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.security.SessionStore;
import vip.geekclub.framework.security.UserAuthentication;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.security.application.command.credential.PasswordVerificationCommand;
import vip.geekclub.support.CaptchaKit;

/**
 * 学生认证控制器
 * 处理学生登录功能
 */
@Slf4j
@RestController("STUDENT_AuthController")
@RequiredArgsConstructor
@RequestMapping("/student/auth")
public class AuthController {

    private final SessionStore authSessionManager;
    private final CaptchaKit captchaKit;

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;
    private final CommandBus commandBus;

    /**
     * 学生登录
     *
     * @param request 登录请求
     * @return JWT Token
     */
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid StudentLoginRequest request) {

        // 非开发环境才验证验证码
        if (!"dev".equals(activeProfile)) {
            var result = captchaKit.validate(request.captchaKey(), request.captcha());
            if (result.isFail()) {
                return ApiResponse.fail(400, result.errorMessage());
            }
        }

        PasswordVerificationCommand command = new PasswordVerificationCommand(
                UserType.STUDENT, request.identifier(), request.password());
        String authId = commandBus.dispatch(command);

        UserPrincipal userPrincipal = new UserPrincipal(authId, UserType.STUDENT);
        UserAuthentication userAuthentication = new UserAuthentication(userPrincipal);
        String jwtToken = authSessionManager.create(userAuthentication);

        return ApiResponse.success(jwtToken);
    }

    /**
     * 生成验证码
     *
     * @return 验证码结果
     */
    @GetMapping("/captcha")
    public ApiResponse<CaptchaKit.CaptchaResult> captcha() {
        CaptchaKit.CaptchaResult result = captchaKit.generate();
        return ApiResponse.success(result);
    }

    /**
     * 学生登录请求
     */
    public record StudentLoginRequest(
            @NotBlank(message = "用户名不能为空") String identifier,
            @NotBlank(message = "密码不能为空") String password,
            @NotBlank(message = "验证码不能为空") String captcha,
            @NotBlank(message = "验证码KEY不能为空") String captchaKey
    ) {
    }
}
