package vip.geekclub.security.permission.application.command.dto;

import vip.geekclub.common.command.Command;
import vip.geekclub.common.utils.StringUtil;

public record UpdatePermissionCommand(
    Long id,
    String name,
    String code,
    String description
) implements Command {
    
    public UpdatePermissionCommand {
        // 字符串字段trim处理
        name = StringUtil.trimToNull(name);
        code = StringUtil.trimToNull(code);
        description = StringUtil.trimToNull(description);
    }
}