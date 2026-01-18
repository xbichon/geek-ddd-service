package vip.geekclub.security.application.command.dto;

import jakarta.validation.constraints.NotNull;
import vip.geekclub.framework.command.Command;
import java.util.Set;

public record UpdatePrincipalRoleCommand(
    @NotNull(message = "用户ID不能为空")
    Long id,
    Set<Long> roles
) implements Command {
}