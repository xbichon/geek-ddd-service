package vip.geekclub.security.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerMapping;
import vip.geekclub.security.application.command.CreatePrincipalCommandHandler;
import vip.geekclub.security.domain.value.CredentialType;

import java.util.UUID;

@CommandHandlerMapping(CreatePrincipalCommandHandler.class)
public record CreatePrincipalCommand(
        @NotBlank(message = "标识符不能为空")
        String identifier,

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
        String password,

        @NotNull(message = "外部用户ID不能为空")
        UUID authId,

        @NotNull(message = "应用类型不能为空")
        String appType,

        @NotNull(message = "认证类型不能为空")
        CredentialType credentialType
) implements Command {
}