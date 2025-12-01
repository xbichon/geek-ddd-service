package vip.geekclub.manager.command.dto;

import jakarta.validation.constraints.NotNull;
import vip.geekclub.common.command.Command;

/**
 * 删除部门命令
 *
 * @author geekclub
 * @since 1.0
 */
public record DeleteDepartmentCommand(

        @NotNull(message = "部门ID不能为空")
        Long id

) implements Command {
}