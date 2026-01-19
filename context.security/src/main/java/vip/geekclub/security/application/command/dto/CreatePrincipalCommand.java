package vip.geekclub.security.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vip.geekclub.framework.command.Command;
import vip.geekclub.security.domain.value.CredentialType;

import java.util.UUID;

public record CreatePrincipalCommand(
        @NotNull(message = "用户类型不能为空")
        String userType,

        @NotBlank(message = "标识符不能为空")
        String identifier,

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
        String password,

        @NotNull(message = "外部用户ID不能为空")
        UUID externalUuid,

        @NotNull(message = "认证类型不能为空")
        CredentialType credentialType
) implements Command {
}