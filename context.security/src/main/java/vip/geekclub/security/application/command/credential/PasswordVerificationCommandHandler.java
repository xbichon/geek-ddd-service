package vip.geekclub.security.application.command.credential;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.exception.InvalidCredentialsException;
import vip.geekclub.security.domain.repository.PasswordCredentialRepository;


@AllArgsConstructor
@Service
public class PasswordVerificationCommandHandler implements CommandHandler<PasswordVerificationCommand, String> {

    private final PasswordCredentialRepository passwordCredentialRepository;

    @Override
    @Transactional(readOnly = true)
    public String execute(PasswordVerificationCommand command) {

        // 1. 获取该用户的密码凭证
        var credential = passwordCredentialRepository.findByIdentifiersValueAndIdentifiersUserType(command.identifier(), command.userType())
                .orElseThrow(() -> new InvalidCredentialsException("账户未找到"));

        // 2. 验证用户名和密码
        credential.verifyPassword(command.password());

        // 3. 直接返回认证标识（冗余存储，无需查询Principal表）
        return credential.getAuthId();
    }
}