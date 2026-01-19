package vip.geekclub.security.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.utils.StringUtil;
import vip.geekclub.security.domain.value.CredentialType;

import java.util.UUID;

public record CreateCredentialCommand(@NotBlank UUID externalUuid
        , @NotNull CredentialType credentialType
        , @NotBlank String identifier
        , @NotBlank String password) implements Command {
    public CreateCredentialCommand {
        identifier = StringUtil.trim(identifier);
        password = StringUtil.trim(password);
    }
}
