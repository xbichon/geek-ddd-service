package vip.geekclub.security.adapter.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UserNameLoginRequest(
        @NotBlank(message = "用户名不能为空")
        String username,
        @NotBlank(message = "密码不能为空")
        String password,
        @NotBlank(message = "验证码不能为空")
        String captcha,
        @NotBlank(message = "验证码KEY不能为空")
        String captchaKey
) {
}
