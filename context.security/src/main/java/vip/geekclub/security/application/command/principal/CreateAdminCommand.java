package vip.geekclub.security.application.command.principal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.util.StringUtils;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerMapping;
import vip.geekclub.security.domain.value.IdentifierValue;

import java.util.List;

@CommandHandlerMapping(CreateAdminCommandHandler.class)
public record CreateAdminCommand(
        @NotBlank @Min(value =1, message = "用户ID不能为空")
        List<IdentifierValue> identifierValues,

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
        String password,

        @NotBlank(message = "认证标识不能为空")
        String authId,

        @NotNull(message = "应用类型不能为空")
        String userType
) implements Command {

    public CreateAdminCommand {
        password = StringUtils.trimAllWhitespace(password);
        authId = StringUtils.trimAllWhitespace(authId);
    }
}