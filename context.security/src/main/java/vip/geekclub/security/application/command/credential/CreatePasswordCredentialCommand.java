package vip.geekclub.security.application.command.credential;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.util.StringUtils;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerMapping;
import vip.geekclub.security.domain.model.Identifier;
import vip.geekclub.security.domain.value.IdentifierType;

import java.util.List;
import java.util.UUID;

@CommandHandlerMapping(CreatePasswordCredentialCommandHandler.class)
public record CreatePasswordCredentialCommand(
        @NotBlank(message = "用户ID不能为空") UUID authId,
        @NotBlank(message = "标识符不能为空") String identifier,
        @NotNull(message = "标识符类型不能为空") IdentifierType credentialType,
        @NotBlank(message = "密码不能为空") @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间") String password,
        @NotNull(message = "是否与现有凭证合并不能为空") Boolean mergeWithExisting
) implements Command {

    public CreatePasswordCredentialCommand {
        identifier = StringUtils.trimAllWhitespace(identifier);
        password = StringUtils.trimAllWhitespace(password);
    }

    /**
     * 转换为领域标识符列表
     */
    public List<Identifier> toIdentifiers() {
        return List.of(Identifier.builder()
            .type(credentialType())
            .value(identifier())
            .build());
    }
}