package vip.geekclub.security.adapter.controller.dto;

/**
 * 验证码结果
 * 包含验证码图片的URL和用于验证的UUID key
 */
public record CaptchaResponse(


    /*
        验证码的UUID key，用于验证时匹配
     */
        String key,

    /*
      验证码图片的Base64编码
     */
        String data
) {
}