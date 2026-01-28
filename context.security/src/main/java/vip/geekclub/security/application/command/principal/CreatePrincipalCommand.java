package vip.geekclub.security.application.command.principal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerMapping;
import vip.geekclub.security.application.command.dto.PasswordCredentialDto;

import java.util.Set;
import java.util.UUID;

@CommandHandlerMapping(CreatePrincipalCommandHandler.class)
public record CreatePrincipalCommand(
        String identifier,
        @NotNull(message = "外部用户ID不能为空") UUID authId,
        @NotNull(message = "应用类型不能为空") String userType,
        Set<Long> roleIds,
        @NotNull(message = "凭证不能为空") @Valid PasswordCredentialDto credential
) implements Command {
}