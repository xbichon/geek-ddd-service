package vip.geekclub.security.adapter.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import vip.geekclub.framework.command.CommandDispatcher;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.security.SessionStore;
import vip.geekclub.framework.security.UserAuthentication;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.security.adapter.controller.dto.CaptchaResponse;
import vip.geekclub.security.adapter.controller.dto.PasswordLoginRequest;
import vip.geekclub.security.application.command.credential.PasswordLoginCommand;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

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
            // 验证验证码
            String redisKey = "captcha:" + request.captchaKey();

            // 从Redis中获取验证码
            String storedCaptcha = stringRedisTemplate.opsForValue().get(redisKey);
            if (storedCaptcha == null) {
                return ApiResponse.fail(400, "验证码已过期或不存在");
            }

            // 无论验证成功与否，都删除验证码
            stringRedisTemplate.delete(redisKey);

            // 检查验证码是否正确（忽略大小写）
            if (!storedCaptcha.equalsIgnoreCase(request.captcha())) {
                return ApiResponse.fail(400, "验证码错误");
            }
        }

        // 验证码验证通过，继续执行登录逻辑
        PasswordLoginCommand command = new PasswordLoginCommand(request.userType(), request.identifier(), request.password());
        CommandResult<UserPrincipal> commandResult = CommandDispatcher.dispatch(command);
        UserPrincipal userPrincipal = commandResult.data();
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
        // 定义图片的宽和高
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);

        // 生成UUID作为KEY
        String captchaKey = UUID.randomUUID().toString();
        String redisKey = "captcha:" + captchaKey;

        // 将验证码文本存储到Redis中，设置2分钟过期
        String captchaText = lineCaptcha.getCode();
        stringRedisTemplate.opsForValue().set(redisKey, captchaText, 120, TimeUnit.SECONDS);

        log.debug("生成验证码: {}, UUID Key: {}", captchaText, captchaKey);

        // 将图片转换为Base64编码
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        lineCaptcha.write(outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        String imageData = "data:image/png;base64," + base64Image;

        // 返回验证码结果，包含图片Base64编码和UUID key
        CaptchaResponse result = new CaptchaResponse(captchaKey, imageData);
        return ApiResponse.success(result);
    }
}