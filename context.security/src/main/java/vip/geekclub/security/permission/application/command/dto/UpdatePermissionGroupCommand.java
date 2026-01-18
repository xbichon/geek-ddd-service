package vip.geekclub.security.permission.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import vip.geekclub.common.command.Command;
import vip.geekclub.common.utils.StringUtil;

public record UpdatePermissionGroupCommand(
        @NotNull(message = "权限组ID不能为空")
        Long id,

        @NotBlank(message = "权限组名称不能为空")
        @Size(max = 20, message = "权限组名称长度不能超过20个字符")
        String name,

        @Size(max = 100, message = "权限组描述长度不能超过100个字符")
        String description,

        @Min(value = 0, message = "排序值不能小于0")
        @Max(value = 100, message = "排序值不能超过100")
        Integer sortOrder

) implements Command {
    public UpdatePermissionGroupCommand {
        // 字符串字段trim处理
        name = StringUtil.trimToNull(name);
        description = StringUtil.trimToNull(description);
    }
}