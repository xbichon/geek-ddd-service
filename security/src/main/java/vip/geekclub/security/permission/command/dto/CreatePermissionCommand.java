package vip.geekclub.security.permission.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vip.geekclub.common.command.Command;
import vip.geekclub.common.utils.StringUtil;

public record CreatePermissionCommand(

    String name,

    @NotBlank(message = "授权码不能为空")
    @Size(max = 50, message = "授权码长度不能超过50")
    String code,

    @Size(max = 100, message = "描述长度不能超过100")
    String description,
    
    Long permissionGroupId
) implements Command {
    
    public CreatePermissionCommand {
        // 字符串字段trim处理
        name = StringUtil.trimToNull(name);
        code = StringUtil.trimToNull(code);
        description = StringUtil.trimToNull(description);
    }
}