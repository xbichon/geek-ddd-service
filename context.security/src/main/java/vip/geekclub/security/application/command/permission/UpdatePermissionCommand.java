package vip.geekclub.security.application.command.permission;

import vip.geekclub.framework.command.Command;
import vip.geekclub.support.StringUtil;

public record UpdatePermissionCommand(
    Long id,
    String name,
    String code,
    String description
) implements Command<Void> {
    
    public UpdatePermissionCommand {
        // 字符串字段trim处理
        name = StringUtil.trimToNull(name);
        code = StringUtil.trimToNull(code);
        description = StringUtil.trimToNull(description);
    }
}