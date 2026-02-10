package vip.geekclub.security.application.command.principal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerMapping;
import vip.geekclub.security.domain.value.IdentifierValue;

import java.util.List;
import java.util.Set;

@CommandHandlerMapping(CreatePrincipalCommandHandler.class)
public record CreatePrincipalCommand(
        @NotNull(message = "应用类型不能为空") String userType,
        @NotNull(message = "外部用户ID不能为空") String authId,

        @Valid
        @NotEmpty(message = "标识符不能为空")
        @Size(min = 1, max = 3, message = "最多只能添加3个标识符")
        List<IdentifierValue> identifierValues,

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
        String password,

        Set<Long> roleIds

) implements Command {

    /**
     * 简化构造函数，无需传递角色ID
     */
    public CreatePrincipalCommand(String userType, String authId,
                                  List<IdentifierValue> identifierValues,
                                  String password) {
        this(userType, authId, identifierValues, password, Set.of());
    }

    public CreatePrincipalCommand {
        if (roleIds == null) {
            roleIds = Set.of();
        }
    }
}