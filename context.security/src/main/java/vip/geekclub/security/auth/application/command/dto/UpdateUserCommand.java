package vip.geekclub.security.auth.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vip.geekclub.framework.command.Command;
import java.util.Set;

public record UpdateUserCommand(
    @NotNull(message = "用户ID不能为空")
    Long id,
    
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过50个字符")
    String name,
    
    Set<Long> roles
) implements Command {
}