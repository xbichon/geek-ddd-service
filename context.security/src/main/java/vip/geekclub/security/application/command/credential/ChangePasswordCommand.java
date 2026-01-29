package vip.geekclub.security.application.command.credential;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.util.StringUtils;
import vip.geekclub.framework.command.Command;

@Builder
public record ChangePasswordCommand(
        @NotBlank(message = "旧密码不能为空") String oldPassword,
        @NotBlank(message = "新密码不能为空") @Size(min = 6, max = 20, message = "新密码长度必须在6-20个字符之间") String newPassword
) implements Command {
    public ChangePasswordCommand {
        oldPassword = StringUtils.trimAllWhitespace(oldPassword);
        newPassword = StringUtils.trimAllWhitespace(newPassword);
    }
}