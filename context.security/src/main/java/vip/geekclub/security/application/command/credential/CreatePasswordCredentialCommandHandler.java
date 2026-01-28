package vip.geekclub.security.application.command.credential;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.security.domain.model.PasswordCredential;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.repository.PasswordCredentialRepository;
import vip.geekclub.security.domain.repository.PrincipalRepository;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

@AllArgsConstructor
@Service
public class CreatePasswordCredentialCommandHandler implements CommandHandler<CreatePasswordCredentialCommand, Void> {

    private final PasswordCredentialRepository passwordCredentialRepository;
    private final PrincipalRepository principalRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(CreatePasswordCredentialCommand command) {
        // 1. 查询用户
        Principal principal = principalRepository.findByAuthId(command.authId())
                .orElseThrow(() -> new NotFoundException("用户不存在"));

        // 2. 检查标识符是否已存在
        if (passwordCredentialRepository.existsByIdentifierTypeAndValue(
                command.credentialType(), command.identifier())) {
            throw new AuthenticationAlreadyExistsException("该标识符已被使用");
        }

        // 3. 创建密码凭证
        PasswordCredential credential;

        if (Boolean.TRUE.equals(command.mergeWithExisting())) {
            // 尝试与现有凭证合并
            PasswordCredential existing = passwordCredentialRepository.findByPrincipalId(principal.getId());

            if (existing != null) {
                // 添加新标识符到现有凭证
                existing.addIdentifier(command.toIdentifiers().get(0));
                credential = existing;
            } else {
                // 创建新的密码凭证
                credential = PasswordCredential.create(
                        principal.getId(),
                        command.password(),
                        command.toIdentifiers()
                );
            }
        } else {
            // 不合并，直接创建新凭证
            credential = PasswordCredential.create(
                    principal.getId(),
                    command.password(),
                    command.toIdentifiers()
            );
        }

        passwordCredentialRepository.save(credential);

        return CommandResult.ok();
    }
}