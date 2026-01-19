package vip.geekclub.security.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.utils.StringUtil;

import java.util.UUID;

public record CreateWechatCredentialCommand(UUID userId, @NotBlank String unionId) implements Command {
    public CreateWechatCredentialCommand {
        unionId = StringUtil.trim(unionId);
    }
}
