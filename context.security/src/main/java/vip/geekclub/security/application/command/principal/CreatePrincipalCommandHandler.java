package vip.geekclub.security.application.command.principal;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.security.domain.model.Credential;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.repository.CredentialRepository;
import vip.geekclub.security.domain.repository.PrincipalRepository;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

@AllArgsConstructor
@Service
public class CreatePrincipalCommandHandler implements CommandHandler<CreatePrincipalCommand, Void> {

    private final PrincipalRepository principalRepository;
    private final CredentialRepository credentialRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(CreatePrincipalCommand command) {
        // 从credentials集合中获取第一个凭证（假设至少有一个）
        var credentials = command.credentials();
        if (credentials == null || credentials.isEmpty()) {
            throw new IllegalArgumentException("至少需要一个凭证");
        }

        // 1. 认证信息查重
        for (var credential : credentials) {
            if (credentialRepository.existsByTypeAndIdentifier(credential.credentialType(), credential.identifier())) {
                throw new AuthenticationAlreadyExistsException("该用户的凭证已经存在,不能重复创建");
            }
        }

        // 2. 创建用户领域对象（如果roleIds存在，通过构造函数设置）
        Principal principal = new Principal(command.userType(), command.authId(), command.roleIds());
        principalRepository.save(principal);

        // 3. 创建认证信息
        for (var credential : credentials) {
            credentialRepository.save(new Credential(
                    principal.getId(),
                    credential.credentialType(),
                    credential.identifier(),
                    credential.password()
            ));
        }


        return CommandResult.ok();
    }
}