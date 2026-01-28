package vip.geekclub.security.application.command.principal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerMapping;
import vip.geekclub.security.domain.value.Identifier;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@CommandHandlerMapping(CreatePrincipalCommandHandler.class)
public record CreatePrincipalCommand(
        String identifier,
        @NotNull(message = "外部用户ID不能为空") UUID authId,
        @NotNull(message = "应用类型不能为空") String userType,
        Set<Long> roleIds,

        @Valid
        @NotEmpty(message = "标识符不能为空")
        @Size(min = 1, max = 3, message = "最多只能添加3个标识符")
        List<Identifier> identifiers,

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
        String password
) implements Command {
}