package vip.geekclub.security.auth.command.dto;

import jakarta.validation.constraints.NotBlank;
import vip.geekclub.common.command.Command;
import vip.geekclub.common.utils.StringUtil;

public record CreateWechatCredentialCommand(Long userId, @NotBlank String unionId) implements Command {
    public CreateWechatCredentialCommand {
        unionId = StringUtil.trim(unionId);
    }
}
