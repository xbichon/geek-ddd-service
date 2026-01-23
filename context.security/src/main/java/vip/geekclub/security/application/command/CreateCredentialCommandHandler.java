package vip.geekclub.security.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.security.application.command.dto.CreateCredentialCommand;
import vip.geekclub.security.domain.model.Credential;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.repository.CredentialRepository;
import vip.geekclub.security.domain.repository.PrincipalRepository;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

@AllArgsConstructor
@Service
public class CreateCredentialCommandHandler implements CommandHandler<CreateCredentialCommand, Void> {

    private final CredentialRepository credentialRepository;
    private final PrincipalRepository principalRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(CreateCredentialCommand command) {
        // 1. 认证信息查重
        Principal principal = principalRepository.findByAuthId(command.externalUuid()).orElseThrow(() -> new NotFoundException("用户不存在"));

        // 2. 认证信息查重
        if (credentialRepository.existsByTypeAndPrincipalId(command.credentialType(), principal.getId())) {
            throw new AuthenticationAlreadyExistsException("认证重复");
        }

        // 3. 认证标识查重
        if (credentialRepository.existsByTypeAndIdentifier(command.credentialType(), command.identifier())) {
            throw new AuthenticationAlreadyExistsException("标识已存在");
        }

        // 4. 创建认证信息
        Credential credential = new Credential(principal.getId(), command.credentialType(), command.identifier(), command.password());
        credentialRepository.save(credential);

        return CommandResult.ok();
    }
}
