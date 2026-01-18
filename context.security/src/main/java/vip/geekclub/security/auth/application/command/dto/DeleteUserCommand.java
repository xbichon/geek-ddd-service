package vip.geekclub.security.auth.application.command.dto;

import jakarta.validation.constraints.NotNull;
import vip.geekclub.framework.command.Command;

public record DeleteUserCommand(
    @NotNull(message = "用户ID不能为空")
    Long id
) implements Command {
}