package vip.geekclub.security.adapter.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record WechatLoginRequest(
    @NotBlank(message = "微信授权码不能为空")
    String code
) {
}