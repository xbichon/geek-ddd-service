package vip.geekclub.security.auth.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.security.JwtAuthentication;
import vip.geekclub.security.auth.adapter.controller.dto.UserNameLoginRequest;

/**
 * 用户名密码认证控制器
 * 处理传统的用户名密码登录功能
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UsernamePasswordAuthController {

    private final AuthenticationManager authenticationManager;

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
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        JwtAuthentication userSession =   (JwtAuthentication) authenticationManager.authenticate(authRequest);
        return ApiResponse.success(userSession.getToken());
    }
}