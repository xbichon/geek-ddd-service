package vip.geekclub.security.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.utils.StringUtil;

public record CreateWechatCredentialCommand(Long userId, @NotBlank String unionId) implements Command {
    public CreateWechatCredentialCommand {
        unionId = StringUtil.trim(unionId);
    }
}
