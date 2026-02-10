package vip.geekclub.security.adapter.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordLoginRequest(
        @NotBlank(message = "用户类型不能为空")
        String userType,

        @NotBlank(message = "用户名不能为空")
        String identifier,
        @NotBlank(message = "密码不能为空")
        String password,

        // 用户类型
        String identifierType,

        @NotBlank(message = "验证码不能为空")
        String captcha,
        @NotBlank(message = "验证码KEY不能为空")
        String captchaKey
) {
}
