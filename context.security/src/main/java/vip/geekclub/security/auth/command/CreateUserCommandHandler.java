package vip.geekclub.security.auth.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.command.IdResult;
import vip.geekclub.security.auth.command.dto.CreateUserCommand;
import vip.geekclub.security.auth.domain.*;
import vip.geekclub.security.auth.common.AuthenticationType;
import vip.geekclub.security.permission.exception.AuthenticationAlreadyExistsException;

@AllArgsConstructor
@Service
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand, IdResult> {

    private final UserPrincipalRepository userPrincipalRepository;
    private final CredentialRepository credentialRepository;

    @Override
    @Transactional
    public CommandResult<IdResult> execute(CreateUserCommand command) {
        // 1. 认证信息查重
        validateUsernameNotExists(command.name());

        // 2. 创建用户领域对象
        UserPrincipal userPrincipal = new UserPrincipal(command.userType());
        userPrincipalRepository.save(userPrincipal);

        // 3. 创建认证信息
        Credential credential = Credential.newUsernameAuth(
                userPrincipal.getId(),
                command.name(),
                command.password()
        );
        credentialRepository.save(credential);

        return CommandResult.ok(userPrincipal.getId());
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