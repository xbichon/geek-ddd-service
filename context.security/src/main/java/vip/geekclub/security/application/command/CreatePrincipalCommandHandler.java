package vip.geekclub.security.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.security.application.command.dto.CreatePrincipalCommand;
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
        // 1. 认证信息查重
        if (credentialRepository.existsByTypeAndIdentifier(command.credentialType(), command.identifier())) {
            throw new AuthenticationAlreadyExistsException("该用户的凭证已经存在,不需要重复创建");
        }

        // 2. 创建用户领域对象
        Principal principal = new Principal(command.appType(), command.authId());
        principalRepository.save(principal);

        // 3. 创建认证信息
        Credential credential = new Credential(
                principal.getId(),
                command.credentialType(),
                command.identifier(),
                command.password()
        );
        credentialRepository.save(credential);

        return CommandResult.ok();
    }
}