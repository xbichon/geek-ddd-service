package vip.geekclub.security.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.command.IdResult;
import vip.geekclub.security.application.command.dto.CreatePrincipalCommand;
import vip.geekclub.security.domain.*;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

@AllArgsConstructor
@Service
public class CreatePrincipalCommandHandler implements CommandHandler<CreatePrincipalCommand, IdResult> {

    private final PrincipalRepository principalRepository;
    private final CredentialRepository credentialRepository;

    @Override
    @Transactional
    public CommandResult<IdResult> execute(CreatePrincipalCommand command) {
        // 1. 认证信息查重
        validateUsernameNotExists(command.name());

        // 2. 创建用户领域对象
        Principal principal = new Principal(command.userType());
        principalRepository.save(principal);

        // 3. 创建认证信息
        Credential credential = Credential.newUsernameAuth(
                principal.getId(),
                command.name(),
                command.password()
        );
        credentialRepository.save(credential);

        return CommandResult.ok(principal.getId());
    }

    /**
     * 验证用户名不存在
     */
    private void validateUsernameNotExists(String username) {
        if (credentialRepository.existsByTypeAndIdentifier(AuthenticationType.USERNAME, username)) {
            throw new AuthenticationAlreadyExistsException("该用户的凭证已经存在,不需要重复创建");
        }
    }
}