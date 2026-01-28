package vip.geekclub.security.application.command.credential;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerMapping;
import vip.geekclub.security.domain.value.ThirdPartyType;

import java.util.UUID;

@CommandHandlerMapping(CreateThirdPartyCredentialCommandHandler.class)
public record CreateThirdPartyCredentialCommand(
        @NotBlank(message = "用户ID不能为空") UUID authId,
        @NotNull(message = "第三方提供商不能为空") ThirdPartyType type,
        @NotBlank(message = "标识符不能为空") String identifier
) implements Command {

    public CreateThirdPartyCredentialCommand {
        identifier = StringUtils.trimAllWhitespace(identifier);
    }
}