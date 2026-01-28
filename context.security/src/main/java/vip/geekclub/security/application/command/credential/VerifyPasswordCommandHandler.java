package vip.geekclub.security.application.command.credential;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.repository.PasswordCredentialRepository;
import vip.geekclub.security.domain.repository.PrincipalRepository;


@AllArgsConstructor
@Service
public class VerifyPasswordCommandHandler implements CommandHandler<VerifyPasswordCommand, VerifyPasswordResult> {

    private final PasswordCredentialRepository passwordCredentialRepository;
    private final PrincipalRepository principalRepository;

    @Override
    @Transactional
    public CommandResult<VerifyPasswordResult> execute(VerifyPasswordCommand command) {

        // 1. 获取该用户的密码凭证
        var credential = passwordCredentialRepository.findByIdentifiersValue(command.identifier())
                .orElseThrow(() -> new NotFoundException("账户未找到"));

        // 2. 验证用户名和密码
        credential.verifyPassword(command.password());

        // 3. 获取用户信息
        Principal principal = principalRepository.findById(credential.getPrincipalId())
                .orElseThrow(() -> new NotFoundException("用户未找到"));

        return CommandResult.ok(new VerifyPasswordResult(principal.getAuthId(), principal.getUserType()));
    }
}