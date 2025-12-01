package vip.geekclub.security.auth.command.dto;

import jakarta.validation.constraints.NotNull;
import vip.geekclub.common.command.Command;

public record DeleteUserCommand(
    @NotNull(message = "用户ID不能为空")
    Long id
) implements Command {
}