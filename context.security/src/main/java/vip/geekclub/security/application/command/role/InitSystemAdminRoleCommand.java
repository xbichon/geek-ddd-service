package vip.geekclub.security.application.command.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerMapping;
import vip.geekclub.support.StringUtil;

/**
 * 初始化系统管理员角色命令
 */
@CommandHandlerMapping(InitSystemAdminRoleCommandHandler.class)
public record InitSystemAdminRoleCommand(
        @NotBlank(message = "用户类型不能为空")
        @Size(min = 1, max = 20, message = "用户类型长度必须在1-20之间")
        String userType
) implements Command<Void> {

    public InitSystemAdminRoleCommand {
        userType = StringUtil.trimToNull(userType);
    }
}
