package vip.geekclub.support;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 * 提供验证码生成、存储和验证功能
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CaptchaKit {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String CAPTCHA_KEY_PREFIX = "captcha:";
    private static final long CAPTCHA_EXPIRE_SECONDS = 120;
    private static final int CAPTCHA_WIDTH = 200;
    private static final int CAPTCHA_HEIGHT = 100;

    /**
     * 验证码结果
     *
     * @param key 验证码key
     * @param data  图片Base64数据
     */
    public record CaptchaResult(String key, String data) {
    }

    /**
     * 验证结果
     *
     * @param success      是否验证通过
     * @param errorMessage 错误信息，验证通过时为null
     */
    public record ValidationResult(boolean success, String errorMessage) {
        public static ValidationResult ok() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult fail(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isFail() {
            return !success;
        }
    }

    /**
     * 生成验证码
     *
     * @return 验证码结果，包含key和图片Base64
     */
    public CaptchaResult generate() {
        // 创建验证码图片
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT);

        // 生成UUID作为KEY
        String captchaKey = UUID.randomUUID().toString();
        String redisKey = CAPTCHA_KEY_PREFIX + captchaKey;

        // 获取验证码文本并存储到Redis
        String captchaText = lineCaptcha.getCode();
        stringRedisTemplate.opsForValue().set(redisKey, captchaText, CAPTCHA_EXPIRE_SECONDS, TimeUnit.SECONDS);

        log.debug("生成验证码: {}, UUID Key: {}", captchaText, captchaKey);

        // 转换为Base64
        String imageData = convertToBase64(lineCaptcha);

        return new CaptchaResult(captchaKey, imageData);
    }

    /**
     * 验证验证码
     *
     * @param captchaKey  验证码key
     * @param captchaCode 用户输入的验证码
     * @return 验证结果
     */
    public ValidationResult validate(String captchaKey, String captchaCode) {
        String redisKey = CAPTCHA_KEY_PREFIX + captchaKey;

        // 从Redis获取验证码
        String storedCaptcha = stringRedisTemplate.opsForValue().get(redisKey);
        if (storedCaptcha == null) {
            return ValidationResult.fail("验证码已过期或不存在");
        }

        // 无论验证成功与否，都删除验证码（一次性使用）
        stringRedisTemplate.delete(redisKey);

        // 检查验证码（忽略大小写）
        if (!storedCaptcha.equalsIgnoreCase(captchaCode)) {
            return ValidationResult.fail("验证码错误");
        }

        return ValidationResult.ok();
    }

    /**
     * 将验证码图片转换为Base64
     */
    private String convertToBase64(LineCaptcha lineCaptcha) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        lineCaptcha.write(outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        return "data:image/png;base64," + base64Image;
    }
}
