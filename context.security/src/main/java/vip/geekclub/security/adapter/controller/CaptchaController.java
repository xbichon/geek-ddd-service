package vip.geekclub.security.adapter.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.security.adapter.controller.dto.CaptchaResponse;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 验证码控制器
 * 用于生成和管理验证码
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class CaptchaController {

    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成验证码
     * 生成验证码图片和UUID key并一起返回给前端
     *
     * @return 验证码结果，包含图片Base64编码和UUID key
     */
    @GetMapping("/captcha")
    public ApiResponse<CaptchaResponse> captcha()  {
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