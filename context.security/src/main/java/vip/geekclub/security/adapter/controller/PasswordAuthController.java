package vip.geekclub.security.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.security.AuthSessionManager;
import vip.geekclub.framework.security.AuthSessionManagerImpl;
import vip.geekclub.framework.security.UserAuthenticationToken;
import vip.geekclub.security.adapter.controller.dto.UserNameLoginRequest;

/**
 * 用户名密码认证控制器
 * 处理传统的用户名密码登录功能
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class PasswordAuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthSessionManager authSessionManager;

    /**
     * 用户名密码登录
     *
     * @param request 登录请求
     * @return JWT Token
     */
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid UserNameLoginRequest request) {
//        // 验证验证码
//        String captchaKey = request.captchaKey();
//        String redisKey = "captcha:" + captchaKey;
//
//        // 从Redis中获取验证码
//        String storedCaptcha = stringRedisTemplate.opsForValue().get(redisKey);
//
//        // 无论验证成功与否，都删除验证码
//        stringRedisTemplate.delete(redisKey);
//
//        // 检查验证码是否存在
//        if (storedCaptcha == null) {
//            return ApiResponse.fail(400, "验证码已过期或不存在");
//        }
//
//        // 检查验证码是否正确（忽略大小写）
//        if (!storedCaptcha.equalsIgnoreCase(request.captcha())) {
//            return ApiResponse.fail(400, "验证码错误");
//        }

        // 验证码验证通过，继续执行登录逻辑
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        UserAuthenticationToken userAuthenticationToken = (UserAuthenticationToken) authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String jwtToken = authSessionManager.createSession(userAuthenticationToken);
        return ApiResponse.success(jwtToken);
    }
}