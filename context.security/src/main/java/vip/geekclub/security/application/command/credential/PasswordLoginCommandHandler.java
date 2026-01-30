package vip.geekclub.security.application.command.credential;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.InvalidCredentialsException;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.repository.PasswordCredentialRepository;
import vip.geekclub.security.domain.repository.PrincipalRepository;


@AllArgsConstructor
@Service
public class PasswordLoginCommandHandler implements CommandHandler<PasswordLoginCommand, UserPrincipal> {

    private final PasswordCredentialRepository passwordCredentialRepository;
    private final PrincipalRepository principalRepository;

    @Override
    @Transactional
    public CommandResult<UserPrincipal> execute(PasswordLoginCommand command) {

        // 1. 获取该用户的密码凭证
        var credential = passwordCredentialRepository.findByIdentifiersValueAndIdentifiersUserType(command.identifier(), command.userType())
                .orElseThrow(() -> new InvalidCredentialsException("账户未找到"));

        // 2. 验证用户名和密码
        credential.verifyPassword(command.password());

        // 3. 获取用户信息
        Principal principal = principalRepository.findById(credential.getPrincipalId())
                .orElseThrow(() -> new InvalidCredentialsException("用户未找到"));

        return CommandResult.ok(new UserPrincipal(principal.getAuthId(), principal.getUserType()));
    }
}