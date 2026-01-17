package vip.geekclub.manager.command.dto;

import jakarta.validation.constraints.NotNull;
import vip.geekclub.common.command.Command;

/**
 * 删除教师命令
 *
 * @author geekclub
 * @since 1.0
 */
public record DeleteTeacherCommand(

        @NotNull(message = "教师ID不能为空")
        Long id

) implements Command {
}