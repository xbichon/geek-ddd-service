package vip.geekclub.security.application.command.principal;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.security.domain.value.Identifier;
import vip.geekclub.security.domain.model.PasswordCredential;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.repository.PasswordCredentialRepository;
import vip.geekclub.security.domain.repository.PrincipalRepository;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

@AllArgsConstructor
@Service
public class CreatePrincipalCommandHandler implements CommandHandler<CreatePrincipalCommand, Void> {

    private final PrincipalRepository principalRepository;
    private final PasswordCredentialRepository passwordCredentialRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(CreatePrincipalCommand command) {
        var credential = command.credential();

        // 1. 检查所有标识符是否已存在
        for (var identifier : credential.identifiers()) {
            if (passwordCredentialRepository.existsByIdentifierTypeAndValue(
                    identifier.type(), identifier.value())) {
                throw new AuthenticationAlreadyExistsException("该标识符已被使用: " + identifier.value());
            }
        }

        // 2. 创建用户领域对象
        Principal principal = new Principal(command.userType(), command.authId(), command.roleIds());
        principalRepository.save(principal);

        // 3. 将 DTO 转换为领域对象
        var identifiers = credential.identifiers().stream()
                .map(dto -> Identifier.builder()
                        .type(dto.type())
                        .value(dto.value())
                        .build())
                .toList();

        // 4. 创建密码凭证
        PasswordCredential passwordCredential = PasswordCredential.create(
                principal.getId(),
                credential.password(),
                identifiers
        );
        passwordCredentialRepository.save(passwordCredential);

        return CommandResult.ok();
    }
}