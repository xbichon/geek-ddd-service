package vip.geekclub.security.application.command.principal;

import jakarta.validation.constraints.NotNull;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerMapping;
import vip.geekclub.security.application.command.dto.CredentialDto;

import java.util.Set;
import java.util.UUID;

@CommandHandlerMapping(CreatePrincipalCommandHandler.class)
public record CreatePrincipalCommand(String identifier, @NotNull(message = "外部用户ID不能为空") UUID authId,
                                     @NotNull(message = "应用类型不能为空") String userType,
                                     @NotNull(message = "凭证不能为空") Set<CredentialDto> credentials,
                                     Set<Long> roleIds) implements Command {
}