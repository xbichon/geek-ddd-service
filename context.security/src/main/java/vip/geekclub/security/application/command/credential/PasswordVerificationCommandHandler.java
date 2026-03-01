package vip.geekclub.security.application.command.credential;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.exception.InvalidCredentialsException;
import vip.geekclub.security.domain.model.Identifier;
import vip.geekclub.security.domain.repository.IdentifierRepository;
import vip.geekclub.security.domain.repository.PasswordCredentialRepository;


@AllArgsConstructor
@Service
public class PasswordVerificationCommandHandler implements CommandHandler<PasswordVerificationCommand, String> {

    private final PasswordCredentialRepository passwordCredentialRepository;
    private final IdentifierRepository identifierRepository;

    @Override
    @Transactional(readOnly = true)
    public String execute(PasswordVerificationCommand command) {
        // 1. 根据标识符找到对应的用户
        Identifier identifier = identifierRepository.findByValueAndUserType(command.identifier(), command.userType())
                .orElseThrow(() -> new InvalidCredentialsException("账户未找到"));

        // 2. 获取该用户的密码凭证
        var credential = passwordCredentialRepository.findByPrincipalId(identifier.getPrincipalId())
                .orElseThrow(() -> new InvalidCredentialsException("账户未找到"));

        // 3. 验证密码
        credential.verifyPassword(command.password());

        // 4. 返回认证标识
        return credential.getAuthId();
    }
}