package vip.geekclub.security.adapter.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record WechatBindRequest(
        @NotBlank(message = "微信授权码不能为空")
        String code,
        @NotBlank(message = "用户标识不能为空")
        String identify,
        @NotBlank(message = "密码不能为空")
        String password
) {
}
