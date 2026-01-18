package vip.geekclub.security.application.command.dto;

import jakarta.validation.constraints.NotNull;
import vip.geekclub.framework.command.Command;

public record DeletePrincipalCommand(
    @NotNull(message = "用户ID不能为空")
    Long id
) implements Command {
}